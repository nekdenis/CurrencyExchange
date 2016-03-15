package nekdenis.github.com.exchangerates.data;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube;
import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube1;
import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube2;
import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;

public class ExchangeRatesConverterTest {

    @Test
    public void testConvert() throws Exception {
        ExchangeRatesResponse ratesResponse = new ExchangeRatesResponse();
        ratesResponse.cube2 = new Cube2();
        ratesResponse.cube2.cube1 = new Cube1();
        ratesResponse.cube2.cube1.time = "2010-10-01";
        ArrayList<Cube> cubes = new ArrayList<>();

        Cube rate1 = new Cube();
        rate1.currency = "USD";
        rate1.rate = 1.3d;
        cubes.add(rate1);

        Cube rate2 = new Cube();
        rate2.currency = "GBP";
        rate2.rate = 0.8d;
        cubes.add(rate2);

        ratesResponse.cube2.cube1.ratesList = cubes;

        ExchangeRates convertedRates = ExchangeRatesConverter.convert(ratesResponse);
        Assert.assertEquals(1285876800000l, convertedRates.getTimestamp());
        Assert.assertEquals(2, convertedRates.getExchangeRatesMap().size());
        Assert.assertEquals(1.3d, convertedRates.getExchangeRate(ExchangeRates.RATE_USD),.01);
        Assert.assertEquals(0.8d, convertedRates.getExchangeRate(ExchangeRates.RATE_GBP),.01);

    }
}