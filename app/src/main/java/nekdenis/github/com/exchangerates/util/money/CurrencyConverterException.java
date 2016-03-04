package nekdenis.github.com.exchangerates.util.money;

public class CurrencyConverterException extends Exception {

    public CurrencyConverterException(String detailMessage) {
        super("Can't convert currency because: " + detailMessage);
    }
}
