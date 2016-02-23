package nekdenis.github.com.exchangerates.data;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRates {

    public static final String RATE_USD = "USD";
    public static final String RATE_GBP = "GBP";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({RATE_USD, RATE_GBP})
    public @interface RATES {
    }


    private long timestamp;
    private Map<String, Float> exchangeRatesMap;

    public ExchangeRates(long timestamp) {
        this.timestamp = timestamp;
        exchangeRatesMap = new HashMap<>();
    }

    public void addRate(String name, Float value) {
        exchangeRatesMap.put(name, value);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Float> getExchangeRatesMap() {
        return exchangeRatesMap;
    }

    public Float getExchangeRate(@RATES String rateName) {
        return exchangeRatesMap.get(rateName);
    }
}
