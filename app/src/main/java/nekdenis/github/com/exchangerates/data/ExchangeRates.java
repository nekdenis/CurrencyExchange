package nekdenis.github.com.exchangerates.data;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Object that represents exchange rates that convert using EUR exchange rate
 */
public class ExchangeRates {

    public static final String RATE_USD = "USD";
    public static final String RATE_EUR = "EUR";
    public static final String RATE_GBP = "GBP";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({RATE_USD, RATE_GBP, RATE_EUR})
    public @interface RATES {
    }


    private long timestamp;
    private Map<String, Double> exchangeRatesMap;

    public ExchangeRates(long timestamp) {
        this.timestamp = timestamp;
        exchangeRatesMap = new HashMap<>();
    }

    public void addRate(String name, Double value) {
        exchangeRatesMap.put(name, value);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Double> getExchangeRatesMap() {
        return exchangeRatesMap;
    }

    public Double getExchangeRate(@RATES String rateName) {
        if (rateName.equals(RATE_EUR)) {
            return 1d;
        } else {
            return exchangeRatesMap.get(rateName);
        }
    }
}
