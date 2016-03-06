package nekdenis.github.com.exchangerates.data;

import java.math.BigDecimal;

import nekdenis.github.com.exchangerates.util.money.CurrencyFormat;


/**
 * Object that represents currency and amount.
 */
public class CurrencyObj {

    private String name;

    private BigDecimal value;

    public CurrencyObj(String name) {
        this.name = name;
        value = new BigDecimal("0");
    }

    public CurrencyObj(CurrencyObj currencyObj) {
        this(currencyObj.getName());
        value = new BigDecimal(currencyObj.getValue().toString());
    }

    public String getName() {
        return name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getValueString() {
        return CurrencyFormat.formatDecimalValue(value);
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
