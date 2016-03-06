package nekdenis.github.com.exchangerates.util.money;

import junit.framework.Assert;

import org.junit.Test;

import java.math.BigDecimal;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class CurrencyCoverterTest {

    @Test
    public void testConvertValue() throws Exception {
        ExchangeRates rates = new ExchangeRates(0l);
        rates.addRate(ExchangeRates.RATE_USD, 1.3d);
        rates.addRate(ExchangeRates.RATE_EUR, 1.0d);
        rates.addRate(ExchangeRates.RATE_GBP, 0.7d);
        rates.addRate("RUB", 80d);

        Assert.assertEquals("13.00", CurrencyCoverter.convertValue(new BigDecimal(10), ExchangeRates.RATE_EUR, ExchangeRates.RATE_USD, rates).toString());

        Assert.assertEquals("7.00", CurrencyCoverter.convertValue(new BigDecimal(10), ExchangeRates.RATE_EUR, ExchangeRates.RATE_GBP, rates).toString());

        Assert.assertEquals("5.38", CurrencyCoverter.convertValue(new BigDecimal(10), ExchangeRates.RATE_USD, ExchangeRates.RATE_GBP, rates).toString());

        //noinspection WrongConstant
        Assert.assertEquals("1.63", CurrencyCoverter.convertValue(new BigDecimal(100), "RUB", ExchangeRates.RATE_USD, rates).toString());
    }
}