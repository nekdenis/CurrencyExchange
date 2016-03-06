package nekdenis.github.com.exchangerates.data;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class CurrencyObjTest {

    @Test
    public void testConstructor() {
        CurrencyObj currencyObj = new CurrencyObj("RUB");
        Assert.assertEquals("RUB", currencyObj.getName());

        CurrencyObj currencyObj2 = new CurrencyObj("THB");
        currencyObj2.setValue(new BigDecimal("10.01"));

        CurrencyObj currencyObj1 = new CurrencyObj(currencyObj2);

        Assert.assertEquals("10.01", currencyObj1.getValueString());
        Assert.assertEquals("THB", currencyObj1.getName());
    }
}