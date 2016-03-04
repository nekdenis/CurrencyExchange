package nekdenis.github.com.exchangerates.util.money;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CurrencyFormat {

    public static String formatFloatRate(Float value) {
        return value.toString();
    }

    public static String formatDecimalValue(BigDecimal result) {
        NumberFormat numberFormat = new DecimalFormat();
        return numberFormat.format(result);
    }
}
