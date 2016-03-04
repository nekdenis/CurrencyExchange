package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.support.annotation.NonNull;

import junit.framework.Assert;

import org.junit.Test;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class ExchangeViewPagerAdapterTest {

    @Test
    public void testGetExchangeRateValue() throws Exception {
        ExchangeConvertedViewPagerAdapter adapter = initAdapter();
        Assert.assertEquals("-", adapter.getCurrencyValue("RUB"));
        Assert.assertEquals("1.1", adapter.getCurrencyValue("USD"));
    }

    @NonNull
    private ExchangeConvertedViewPagerAdapter initAdapter() {
        ExchangeConvertedViewPagerAdapter adapter = new ExchangeConvertedViewPagerAdapter(null);
        ExchangeRates rates = new ExchangeRates(0);
        rates.addRate(ExchangeRates.RATE_USD, 1.1d);
        rates.addRate(ExchangeRates.RATE_GBP, 0.7d);
        adapter.rates = rates;
        return adapter;
    }
}