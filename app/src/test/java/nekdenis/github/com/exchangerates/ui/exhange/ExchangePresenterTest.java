package nekdenis.github.com.exchangerates.ui.exhange;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExchangePresenterTest {

    @Test
    public void testGetSelectedOriginalCurrency() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        presenter.lastSelectedOriginalCurrency = "USD";
        Assert.assertEquals("USD", presenter.getSelectedOriginalCurrency());

        presenter.lastSelectedOriginalCurrency = null;
        presenter.currencies = new ArrayList<>();
        presenter.currencies.add("EUR");
        presenter.currencies.add("USD");
        presenter.currencies.add("GPB");
        Assert.assertEquals("EUR", presenter.getSelectedOriginalCurrency());

        presenter.lastSelectedOriginalCurrency = "aaa";
        Assert.assertEquals("EUR", presenter.getSelectedOriginalCurrency());

    }

    @Test
    public void testGetSelectedConvertedCurrency() throws Exception {
        ExchangePresenter presenter = new ExchangePresenter();
        presenter.lastSelectedConvertedCurrency = "USD";
        Assert.assertEquals("USD", presenter.getSelectedConvertedCurrency());

        presenter.lastSelectedConvertedCurrency = null;
        presenter.currencies = new ArrayList<>();
        presenter.currencies.add("EUR");
        presenter.currencies.add("USD");
        presenter.currencies.add("GPB");
        Assert.assertEquals("EUR", presenter.getSelectedConvertedCurrency());

        presenter.lastSelectedOriginalCurrency = "aaa";
        Assert.assertEquals("EUR", presenter.getSelectedConvertedCurrency());

    }

    @Test
    public void testGetAvailableToConvertCurrencies() throws Exception {
        ArrayList currencies = new ArrayList<>();
        currencies.add("EUR");
        currencies.add("USD");
        currencies.add("GPB");

        ExchangePresenter presenter = new ExchangePresenter();
        presenter.lastSelectedOriginalCurrency = "EUR";
        List availableToConvertCurrencies = presenter.getAvailableToConvertCurrencies(currencies);

        Assert.assertEquals(2, availableToConvertCurrencies.size());
        Assert.assertFalse(availableToConvertCurrencies.contains("EUR"));
    }
}