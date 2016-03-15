package nekdenis.github.com.exchangerates.ui.exhange;

import android.os.Bundle;

import java.util.List;

import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.data.ExchangeRates;

public interface ExchangeInterfaces {

    interface ExchangeViewInterface {

        void updateOriginalCurrencies(List<CurrencyObj> currencies);

        void updateConvertedCurrencies(List<CurrencyObj> currencies);

        void onRatesUpdatingError(String message);

        void notifyRatesUpdated();

        void selectCurrencies(CurrencyObj originalSelectedCurrency, CurrencyObj convertedSelectedCurrency);
    }

    interface ExchangePresenterInterface {

        void loadRates();

        void attachViewInterface(ExchangeInterfaces.ExchangeViewInterface viewInterface);

        void detachViewInterface();

        void onSelectedOriginalCurrencyChanged(CurrencyObj selectedOriginalCurrency);

        void onSelectedConvertedCurrencyChanged(CurrencyObj selectedCurrency);

        void onRateChanged(CurrencyObj selectedCurrency);

        void onSaveInstanceState(Bundle outState);

        void onRestoreInstanceState(Bundle savedState);
    }

    interface ExchangePresenterCallback {

        void onRatesLoadError(Throwable e);

        void onRatesLoaded(ExchangeRates rates);
    }

    interface ExchangeModelInterface {

        void detach();

        void doGetRatesRequest();
    }

}
