package nekdenis.github.com.exchangerates.ui.exhange;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.BuildConfig;
import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.util.money.CurrencyConverterException;
import nekdenis.github.com.exchangerates.util.money.CurrencyCoverter;


/**
 * Presenter for {@link ExchangeInterfaces.ExchangeViewInterface} that handles all business logic
 * related to load, show and convert exchange rates
 */
public class ExchangePresenter implements ExchangeInterfaces.ExchangePresenterInterface, ExchangeInterfaces.ExchangePresenterCallback {

    public static final BigDecimal CURRENCY_UNKNOWN_VALUE = new BigDecimal("0");
    private static final String STATE_LAST_SELECTED_ORIGINAL_CURRENCY = "STATE_LAST_SELECTED_ORIGINAL_CURRENCY";
    private static final String STATE_LAST_SELECTED_CONVERTED_CURRENCY = "STATE_LAST_SELECTED_CONVERTED_CURRENCY";
    private static final String STATE_CURRENCIES = "STATE_CURRENCIES";
    private static final String STATE_RATES = "STATE_RATES";

    protected ExchangeInterfaces.ExchangeViewInterface viewInterface;
    protected CurrencyObj lastSelectedOriginalCurrency;
    protected CurrencyObj lastSelectedConvertedCurrency;
    protected List<CurrencyObj> currencies;
    protected ExchangeRates exchangeRates;
    protected ExchangeInterfaces.ExchangeModelInterface exchangeModel;


    public ExchangePresenter() {
        currencies = new ArrayList<>();
        exchangeModel = ExchangeMVPFactory.initModel(this);
    }

    @Override
    public void loadRates() {
        exchangeModel.doGetRatesRequest();
    }

    @Override
    public void attachViewInterface(ExchangeInterfaces.ExchangeViewInterface viewInterface) {
        this.viewInterface = viewInterface;
        loadAvailableCurrencies();
        loadRates();
    }

    public void onRatesLoaded(ExchangeRates rates) {
        this.exchangeRates = rates;
        updateRates();
        if (viewInterface != null && BuildConfig.DEBUG) {
            //only for debug
            viewInterface.notifyRatesUpdated();
        }
    }

    private void loadAvailableCurrencies() {
        List<CurrencyObj> currencies = new ArrayList<>(3);
        currencies.add(new CurrencyObj(ExchangeRates.RATE_GBP));
        currencies.add(new CurrencyObj(ExchangeRates.RATE_USD));
        currencies.add(new CurrencyObj(ExchangeRates.RATE_EUR));
        onCurrenciesLoaded(currencies);
    }

    public void onRatesLoadError(Throwable e) {
        if (viewInterface != null) {
            viewInterface.onRatesUpdatingError(e.getMessage());
        }
    }

    @Override
    public void detachViewInterface() {
        exchangeModel.detach();
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

    @Override
    public void onSelectedOriginalCurrencyChanged(CurrencyObj selectedOriginalCurrency) {
        this.lastSelectedOriginalCurrency = selectedOriginalCurrency;
        onRateChanged(selectedOriginalCurrency);
    }

    @Override
    public void onSelectedConvertedCurrencyChanged(CurrencyObj selectedCurrency) {
        lastSelectedConvertedCurrency = selectedCurrency;
    }

    void updateRates() {
        onRateChanged(getSelectedOriginalCurrency());
    }

    @Override
    public void onRateChanged(CurrencyObj selectedCurrency) {
        List<CurrencyObj> availableToConvertCurrencies = getAvailableToConvertCurrencies(currencies);
        updateAllCurrencies(selectedCurrency, availableToConvertCurrencies);
        if (viewInterface != null) {
            viewInterface.updateConvertedCurrencies(availableToConvertCurrencies);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(STATE_LAST_SELECTED_ORIGINAL_CURRENCY, lastSelectedOriginalCurrency);
        outState.putSerializable(STATE_LAST_SELECTED_CONVERTED_CURRENCY, lastSelectedConvertedCurrency);
        outState.putSerializable(STATE_CURRENCIES, new ArrayList<>(currencies));
        outState.putSerializable(STATE_RATES, exchangeRates);
    }

    @Override
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
