package nekdenis.github.com.exchangerates.util.money;

import junit.framework.Assert;

import org.junit.Test;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class CurrencyCoverterTest {

    @Test
    public void testConvertValue() throws Exception {
        ExchangeRates rates = new ExchangeRates(0l);
        rates.addRate(ExchangeRates.RATE_USD, 1.3d);
        rates.addRate(ExchangeRates.RATE_EUR, 1.0d);
        rates.addRate(ExchangeRates.RATE_GBP, 0.7d);

        Assert.assertEquals("13", CurrencyCoverter.convertValue(10, ExchangeRates.RATE_EUR, ExchangeRates.RATE_USD, rates));

        Assert.assertEquals("7", CurrencyCoverter.convertValue(10, ExchangeRates.RATE_EUR, ExchangeRates.RATE_GBP, rates));

        Assert.assertEquals("5.38", CurrencyCoverter.convertValue(10, ExchangeRates.RATE_USD, ExchangeRates.RATE_GBP, rates));
    }
}