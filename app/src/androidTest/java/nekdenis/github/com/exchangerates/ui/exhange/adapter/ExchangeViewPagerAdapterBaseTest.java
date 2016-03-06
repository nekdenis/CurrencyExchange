package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.data.CurrencyObj;

public class ExchangeViewPagerAdapterBaseTest {

    @Test
    public void testSetData() throws Exception {
        ExchangeViewPagerAdapterBase adapter = initAdapter();

        List<CurrencyObj> currenciesObj = adapter.currencies;
        adapter.setData(new ArrayList<CurrencyObj>());
        Assert.assertEquals(0, adapter.currencies.size());
        ArrayList<CurrencyObj> newCurrencies = new ArrayList<>();

        newCurrencies.add(new CurrencyObj(""));
        adapter.setData(newCurrencies);
        Assert.assertEquals(1, adapter.currencies.size());

        Assert.assertTrue(currenciesObj == adapter.currencies);
    }

    @Test
    public void testGetItemAt() throws Exception {
        ExchangeViewPagerAdapterBase adapter = initAdapter();
        Assert.assertEquals("15.05", adapter.getItemAt(1).getValueString());
        Assert.assertEquals("EUR", adapter.getItemAt(1).getName());
    }

    @Test
    public void testGetCount() throws Exception {
        ExchangeViewPagerAdapterBase adapter = initAdapter();
        Assert.assertEquals(2, adapter.getCount());
    }

    @Test
    public void testGetData() throws Exception {
        ExchangeViewPagerAdapterBase adapter = initAdapter();
        Assert.assertEquals(2, adapter.getData().size());
    }

    @NonNull
    private ExchangeViewPagerAdapterBase initAdapter() {
        ExchangeViewPagerAdapterBase adapter = new ExchangeConvertedViewPagerAdapter(getContext());
        CurrencyObj usd = new CurrencyObj("USD");
        usd.setValue(new BigDecimal("10.01"));
        CurrencyObj eur = new CurrencyObj("EUR");
        eur.setValue(new BigDecimal("15.05"));
        adapter.currencies.add(usd);
        adapter.currencies.add(eur);
        return adapter;
    }

    private Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }
}