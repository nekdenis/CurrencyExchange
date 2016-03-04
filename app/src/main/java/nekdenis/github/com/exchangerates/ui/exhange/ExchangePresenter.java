package nekdenis.github.com.exchangerates.ui.exhange;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.api.Api;
import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.data.ExchangeRatesConverter;
import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;
import nekdenis.github.com.exchangerates.di.Injector;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.Exceptions;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ExchangePresenter {

    private ExchangeViewInterface viewInterface;

    String lastSelectedOriginalCurrency;
    String lastSelectedConvertedCurrency;
    List<String> currencies;
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
        viewInterface.updateCurrencyRates(rates);
    }

    public void attachViewInterface(ExchangeViewInterface viewInterface) {
        this.viewInterface = viewInterface;
        loadAvailableCurrencies();
        loadRates();
    }

    private void loadAvailableCurrencies() {
        List<String> currencies = new ArrayList<>(3);
        currencies.add(ExchangeRates.RATE_GBP);
        currencies.add(ExchangeRates.RATE_USD);
        currencies.add(ExchangeRates.RATE_USD);

        onCurrenciesLoaded(currencies);
    }

    public void doGetRatesRequest() {
        exchangeRateSubscription = service.getLatestRates()
                .subscribeOn(Schedulers.io())
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
                        Log.d("","");
                    }

                    @Override
                    public void onError(Throwable e) {
                        viewInterface.onRatesUpdatingError(e.getMessage());
                    }

                    @Override
                    public void onNext(ExchangeRates response) {
                        onRatesLoaded(response);
                    }
                });
    }

    public void detachViewInterface() {
        if (exchangeRateSubscription != null) {
            exchangeRateSubscription.unsubscribe();
        }
        this.viewInterface = null;
    }

    void onCurrenciesLoaded(List<String> currencies) {
        this.currencies.clear();
        this.currencies.addAll(currencies);

        viewInterface.updateOriginalCurrencies(currencies);

        List<String> availableToConvertCurrencies = getAvailableToConvertCurrencies(currencies);
        viewInterface.updateConvertedCurrencies(availableToConvertCurrencies);

        viewInterface.setSelectedOriginalCurrency(getSelectedOriginalCurrency());
        viewInterface.setSelectedConvertedCurrency(getSelectedConvertedCurrency());
    }

    String getSelectedOriginalCurrency() {
        return (lastSelectedOriginalCurrency != null && currencies.contains(lastSelectedOriginalCurrency)) ? lastSelectedOriginalCurrency : currencies.get(0);
    }

    String getSelectedConvertedCurrency() {
        return (lastSelectedConvertedCurrency != null && currencies.contains(lastSelectedConvertedCurrency)) ? lastSelectedConvertedCurrency : currencies.get(0);
    }

    @NonNull
    List<String> getAvailableToConvertCurrencies(List<String> currencies) {
        List<String> availableToConvertCurrencies = new ArrayList<>(currencies);
        currencies.remove(getSelectedOriginalCurrency());
        return availableToConvertCurrencies;
    }
}
