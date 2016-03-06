package nekdenis.github.com.exchangerates.data;

import org.junit.Assert;
import org.junit.Test;

public class ExchangeRatesTest {

    @Test
    public void testGetExchangeRate() throws Exception {
        ExchangeRates rates = new ExchangeRates(0l);
        rates.addRate(ExchangeRates.RATE_USD, 1.3d);
        rates.addRate(ExchangeRates.RATE_GBP, 0.7d);

        Assert.assertEquals(1, rates.getExchangeRate(ExchangeRates.RATE_EUR), 0.01);
        Assert.assertEquals(0.7, rates.getExchangeRate(ExchangeRates.RATE_GBP), 0.01);
        Assert.assertEquals(1.3, rates.getExchangeRate(ExchangeRates.RATE_USD), 0.01);
    }
}