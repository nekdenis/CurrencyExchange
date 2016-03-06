package nekdenis.github.com.exchangerates.ui.exhange;

import android.os.Bundle;
import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class ExchangePresenterAndroidTest {

    @Test
    public void testRestoringState() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        ExchangeRates exchangeRates = getExchangeRates();
        presenter.exchangeRates = exchangeRates;
        ArrayList<CurrencyObj> currenciesList = getCurrenciesList();
        presenter.currencies = currenciesList;
        CurrencyObj eur = new CurrencyObj("EUR");
        eur.setValue(new BigDecimal(10));
        presenter.lastSelectedOriginalCurrency = eur;
        CurrencyObj usd = new CurrencyObj("USD");
        usd.setValue(new BigDecimal(15));
        presenter.lastSelectedConvertedCurrency = usd;

        Bundle bundle = new Bundle();
        presenter.onSaveInstanceState(bundle);

        presenter = new ExchangePresenter();

        presenter.onRestoreInstanceState(bundle);

        Assert.assertEquals(currenciesList.size(), presenter.currencies.size());
        Assert.assertEquals(exchangeRates.getExchangeRatesMap().values().size(), presenter.exchangeRates.getExchangeRatesMap().values().size());
        Assert.assertEquals(eur.getName(), presenter.lastSelectedOriginalCurrency.getName());
        Assert.assertEquals(usd.getName(), presenter.lastSelectedConvertedCurrency.getName());
    }

    @NonNull
    private ArrayList<CurrencyObj> getCurrenciesList() {
        ArrayList<CurrencyObj> currencies = new ArrayList<>();
        currencies.add(new CurrencyObj("EUR"));
        currencies.add(new CurrencyObj("USD"));
        currencies.add(new CurrencyObj("GBP"));
        return currencies;
    }

    @NonNull
    private ExchangeRates getExchangeRates() {
        ExchangeRates rates = new ExchangeRates(0l);
        rates.addRate("USD", 1.3d);
        rates.addRate("GBP", 0.7d);
        return rates;
    }
}
