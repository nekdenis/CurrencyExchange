package nekdenis.github.com.exchangerates.util.money;

import junit.framework.Assert;

import org.junit.Test;

public class CurrencyFormatTest {

    @Test
    public void testFormatFloatRate() throws Exception {
        Assert.assertEquals("0.0", CurrencyFormat.formatFloatRate(0f));
        Assert.assertEquals("1.0", CurrencyFormat.formatFloatRate(1f));
        Assert.assertTrue("0.01".equals(CurrencyFormat.formatFloatRate(0.01f)) ||
                ("0,01".equals(CurrencyFormat.formatFloatRate(0.01f))));
    }
}