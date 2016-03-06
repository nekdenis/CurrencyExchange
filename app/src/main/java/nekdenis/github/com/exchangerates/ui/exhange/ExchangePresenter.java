package nekdenis.github.com.exchangerates.ui.exhange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nekdenis.github.com.exchangerates.BuildConfig;
import nekdenis.github.com.exchangerates.api.Api;
import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.data.ExchangeRatesConverter;
import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;
import nekdenis.github.com.exchangerates.di.Injector;
import nekdenis.github.com.exchangerates.util.Constants;
import nekdenis.github.com.exchangerates.util.money.CurrencyConverterException;
import nekdenis.github.com.exchangerates.util.money.CurrencyCoverter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Presenter for {@link ExchangeViewInterface} that handles all business logic
 * related to load, show and convert exchange rates
 */
public class ExchangePresenter {

    public static final BigDecimal CURRENCY_UNKNOWN_VALUE = new BigDecimal("0");
    private static final String STATE_LAST_SELECTED_ORIGINAL_CURRENCY = "STATE_LAST_SELECTED_ORIGINAL_CURRENCY";
    private static final String STATE_LAST_SELECTED_CONVERTED_CURRENCY = "STATE_LAST_SELECTED_CONVERTED_CURRENCY";
    private static final String STATE_CURRENCIES = "STATE_CURRENCIES";
    private static final String STATE_RATES = "STATE_RATES";

    ExchangeViewInterface viewInterface;
    CurrencyObj lastSelectedOriginalCurrency;
    CurrencyObj lastSelectedConvertedCurrency;
    List<CurrencyObj> currencies;
    ExchangeRates exchangeRates;
    Api service;

    private Subscription exchangeRateSubscription;

    public ExchangePresenter() {
        currencies = new ArrayList<>();
        service = Injector.provideService();
    }

    public void loadRates() {
        doGetRatesRequest();
    }

    private void onRatesLoaded(ExchangeRates rates) {
        this.exchangeRates = rates;
        updateRates();
        if (viewInterface != null && BuildConfig.DEBUG) {
            //only for debug
            viewInterface.notifyRatesUpdated();
        }
    }

    public void attachViewInterface(ExchangeViewInterface viewInterface) {
        this.viewInterface = viewInterface;
        loadAvailableCurrencies();
        loadRates();
    }

    private void loadAvailableCurrencies() {
        List<CurrencyObj> currencies = new ArrayList<>(3);
        currencies.add(new CurrencyObj(ExchangeRates.RATE_GBP));
        currencies.add(new CurrencyObj(ExchangeRates.RATE_USD));
        currencies.add(new CurrencyObj(ExchangeRates.RATE_EUR));
        onCurrenciesLoaded(currencies);
    }

    private void doGetRatesRequest() {
        exchangeRateSubscription = service.getLatestRates()
                .subscribeOn(Schedulers.io())
                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(Constants.RATES_REPEAT_INTERVAL, TimeUnit.SECONDS);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ExchangeRatesResponse, Observable<ExchangeRates>>() {
                    @Override
                    public Observable<ExchangeRates> call(ExchangeRatesResponse exchangeRatesResponse) {
                        try {
                            return Observable.just(ExchangeRatesConverter.convert(exchangeRatesResponse));
                        } catch (ParseException e) {
                            throw Exceptions.propagate(e);
                        }
                    }
                })
                .subscribe(new Subscriber<ExchangeRates>() {
                    @Override
                    public void onCompleted() {
                        Log.d("", "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRatesLoadError(e);
                    }

                    @Override
                    public void onNext(ExchangeRates response) {
                        onRatesLoaded(response);
                    }
                });
    }

    private void onRatesLoadError(Throwable e) {
        if (viewInterface != null) {
            viewInterface.onRatesUpdatingError(e.getMessage());
        }
    }

    public void detachViewInterface() {
        if (exchangeRateSubscription != null) {
            exchangeRateSubscription.unsubscribe();
        }
        this.viewInterface = null;
    }

    void onCurrenciesLoaded(List<CurrencyObj> currencies) {
        this.currencies.clear();
        this.currencies.addAll(currencies);
        if (viewInterface != null) {
            viewInterface.updateOriginalCurrencies(currencies);
            List<CurrencyObj> availableToConvertCurrencies = getAvailableToConvertCurrencies(currencies);
            viewInterface.updateConvertedCurrencies(availableToConvertCurrencies);
        }
    }

    CurrencyObj getSelectedOriginalCurrency() {
        return (lastSelectedOriginalCurrency != null && currencies.contains(lastSelectedOriginalCurrency)) ? lastSelectedOriginalCurrency : currencies.get(0);
    }

    CurrencyObj getSelectedConvertedCurrency() {
        return (lastSelectedConvertedCurrency != null && currencies.contains(lastSelectedConvertedCurrency)) ? lastSelectedConvertedCurrency : currencies.get(0);
    }

    @NonNull
    List<CurrencyObj> getAvailableToConvertCurrencies(List<CurrencyObj> currencies) {
        List<CurrencyObj> availableToConvertCurrencies = new ArrayList<>(currencies.size() - 1);
        CurrencyObj selectedOriginalCurrency = getSelectedOriginalCurrency();
        for (CurrencyObj currencyObj : currencies) {
            if (!currencyObj.equals(selectedOriginalCurrency)) {
                availableToConvertCurrencies.add(new CurrencyObj(currencyObj));
            }
        }
        return availableToConvertCurrencies;
    }

    public void onSelectedOriginalCurrencyChanged(CurrencyObj selectedOriginalCurrency) {
        this.lastSelectedOriginalCurrency = selectedOriginalCurrency;
        onRateChanged(selectedOriginalCurrency);
    }

    public void onSelectedConvertedCurrencyChanged(CurrencyObj selectedCurrency) {
        lastSelectedConvertedCurrency = selectedCurrency;
    }

    void updateRates() {
        onRateChanged(getSelectedOriginalCurrency());
    }

    public void onRateChanged(CurrencyObj selectedCurrency) {
        List<CurrencyObj> availableToConvertCurrencies = getAvailableToConvertCurrencies(currencies);
        updateAllCurrencies(selectedCurrency, availableToConvertCurrencies);
        if (viewInterface != null) {
            viewInterface.updateConvertedCurrencies(availableToConvertCurrencies);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_LAST_SELECTED_ORIGINAL_CURRENCY, lastSelectedOriginalCurrency);
        outState.putSerializable(STATE_LAST_SELECTED_CONVERTED_CURRENCY, lastSelectedConvertedCurrency);
        outState.putSerializable(STATE_CURRENCIES, new ArrayList<>(currencies));
        outState.putSerializable(STATE_RATES, exchangeRates);
    }

    public void onRestoreInstanceState(Bundle savedState) {
        lastSelectedOriginalCurrency = (CurrencyObj) savedState.getSerializable(STATE_LAST_SELECTED_ORIGINAL_CURRENCY);
        lastSelectedConvertedCurrency = (CurrencyObj) savedState.getSerializable(STATE_LAST_SELECTED_CONVERTED_CURRENCY);
        onCurrenciesLoaded((List<CurrencyObj>) savedState.getSerializable(STATE_CURRENCIES));
        if (viewInterface != null) {
            viewInterface.selectCurrencies(lastSelectedOriginalCurrency, lastSelectedOriginalCurrency);
        }
        onRatesLoaded((ExchangeRates) savedState.getSerializable(STATE_RATES));
    }

    void updateAllCurrencies(CurrencyObj selectedCurrency, List<CurrencyObj> availableToConvertCurrencies) {
        for (int i = 0; i < availableToConvertCurrencies.size(); i++) {
            updateExchangeRateValue(availableToConvertCurrencies.get(i), selectedCurrency, exchangeRates);
        }
    }

    void updateExchangeRateValue(CurrencyObj currencyObjToChange, CurrencyObj selectedCurrency, ExchangeRates exchangeRates) {
        BigDecimal value = null;
        try {
            //noinspection WrongConstant
            value = CurrencyCoverter.convertValue(selectedCurrency.getValue(), selectedCurrency.getName(), currencyObjToChange.getName(), exchangeRates);
        } catch (CurrencyConverterException e) {
            value = CURRENCY_UNKNOWN_VALUE;
        }
        currencyObjToChange.setValue(value);
    }
}
