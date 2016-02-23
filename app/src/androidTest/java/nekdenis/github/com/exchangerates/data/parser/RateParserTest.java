package nekdenis.github.com.exchangerates.data.parser;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class RateParserTest {

    @Test
    public void testParse() throws Exception {
        InputStream stream = getStreamFromAssets("assets/response/rate_test_data.xml");
        RateParser parser = new RateParser();
        ExchangeRates parsedRates = parser.parse(stream);

        Assert.assertEquals(1456074000000l, parsedRates.getTimestamp());
        Assert.assertEquals(1.1026f, parsedRates.getExchangeRate(ExchangeRates.RATE_USD).floatValue(), 0.1f);
        Assert.assertEquals(0.78243f, parsedRates.getExchangeRate(ExchangeRates.RATE_GBP).floatValue(), 0.1f);
    }

    private InputStream getStreamFromAssets(String filePath) throws FileNotFoundException {
        return this.getClass().getClassLoader().getResourceAsStream(filePath);
    }
}