package nekdenis.github.com.exchangerates.ui.exhange;

import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import junit.framework.Assert;

import org.junit.Test;

import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.ui.view.ConverterView;

public class ExchangeViewPagerAdapterTest {

    @Test
    public void testGetConverterView() throws Exception {
        ExchangeViewPagerAdapter adapter = initAdapter();
        ConverterView view = adapter.getConverterView(ExchangeRates.RATE_GBP, "1.1");
        Assert.assertEquals("GPB", view.getCurrencyName());
        Assert.assertEquals("1.1", view.getCurrentCurrencyValue());
    }

    @NonNull
    private ExchangeViewPagerAdapter initAdapter() {
        ExchangeViewPagerAdapter adapter = new ExchangeViewPagerAdapter(false, InstrumentationRegistry.getInstrumentation().getTargetContext());
        ExchangeRates rates = new ExchangeRates(0);
        rates.addRate(ExchangeRates.RATE_USD, 1.1f);
        rates.addRate(ExchangeRates.RATE_GBP, 0.7f);
        adapter.rates = rates;
        return adapter;
    }
}