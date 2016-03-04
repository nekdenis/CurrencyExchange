package nekdenis.github.com.exchangerates.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import nekdenis.github.com.exchangerates.data.response.exchangerates.Cube;
import nekdenis.github.com.exchangerates.data.response.exchangerates.ExchangeRatesResponse;

public class ExchangeRatesConverter {

    private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static ExchangeRates convert(ExchangeRatesResponse response) throws ParseException {
        //TODO: add test
        long timeStamp = timestampFormat.parse(response.cube2.cube1.time).getTime();
        ExchangeRates result = new ExchangeRates(timeStamp);
        for (Cube cube : response.cube2.cube1.ratesList) {
            result.addRate(cube.currency, cube.rate);
        }
        return result;
    }
}
