package nekdenis.github.com.exchangerates.ui.exhange;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class ExchangePresenterTest {

    @Test
    public void testGetSelectedOriginalCurrency() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();

        presenter.lastSelectedOriginalCurrency = null;
        presenter.currencies = getCurrenciesList();
        Assert.assertEquals("EUR", presenter.getSelectedOriginalCurrency().getName());

        presenter.lastSelectedOriginalCurrency = new CurrencyObj("aaa");
        Assert.assertEquals("EUR", presenter.getSelectedOriginalCurrency().getName());

    }

    @Test
    public void testGetSelectedConvertedCurrency() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        presenter.lastSelectedConvertedCurrency = null;
        presenter.currencies = new ArrayList<>();
        presenter.currencies.add(new CurrencyObj("EUR"));
        CurrencyObj usd = new CurrencyObj("USD");
        presenter.currencies.add(usd);
        presenter.currencies.add(new CurrencyObj("GBP"));
        Assert.assertEquals("EUR", presenter.getSelectedConvertedCurrency().getName());

        presenter.lastSelectedConvertedCurrency = new CurrencyObj("aaa");
        Assert.assertEquals("EUR", presenter.getSelectedConvertedCurrency().getName());

        presenter.lastSelectedConvertedCurrency = usd;
        Assert.assertEquals("USD", presenter.getSelectedConvertedCurrency().getName());
    }

    @Test
    public void testGetAvailableToConvertCurrencies() throws Exception {
        ArrayList<CurrencyObj> currencies = getCurrenciesList();

        ExchangePresenter presenter = new ExchangePresenter();
        presenter.currencies = currencies;
        presenter.lastSelectedOriginalCurrency = new CurrencyObj("EUR");
        List availableToConvertCurrencies = presenter.getAvailableToConvertCurrencies(currencies);

        Assert.assertEquals(2, availableToConvertCurrencies.size());
        Assert.assertFalse(availableToConvertCurrencies.contains("EUR"));
    }

    @Test
    public void testDetachViewInterface() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        presenter.viewInterface = new ExchangeViewInterface() {
            @Override
            public void updateOriginalCurrencies(List<CurrencyObj> currencies) {

            }

            @Override
            public void updateConvertedCurrencies(List<CurrencyObj> currencies) {

            }

            @Override
            public void onRatesUpdatingError(String message) {

            }
        };
        presenter.detachViewInterface();
        Assert.assertNull(presenter.viewInterface);
    }

    @Test
    public void testOnCurrenciesLoaded() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        ArrayList<CurrencyObj> currencies = getCurrenciesList();
        presenter.onCurrenciesLoaded(currencies);
        Assert.assertEquals(3, currencies.size());
    }

    @Test
    public void testOnSelectedOriginalCurrencyChanged() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        presenter.currencies = getCurrenciesList();
        presenter.exchangeRates = getExchangeRates();
        CurrencyObj selectedOriginalCurrency = new CurrencyObj("1");
        presenter.onSelectedOriginalCurrencyChanged(selectedOriginalCurrency);
        Assert.assertTrue(presenter.lastSelectedOriginalCurrency == selectedOriginalCurrency);
    }

    @Test
    public void testUpdateAllCurrencies() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        List<CurrencyObj> currencies = getCurrenciesList();
        presenter.exchangeRates = getExchangeRates();
        presenter.exchangeRates.addRate("RUB", 80d);
        CurrencyObj rub = new CurrencyObj("RUB");
        rub.setValue(new BigDecimal("100"));
        presenter.updateAllCurrencies(rub, currencies);
        Assert.assertEquals("USD", currencies.get(1).getName());
        Assert.assertEquals("1.63", currencies.get(1).getValueString());
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