package nekdenis.github.com.exchangerates.util.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class CurrencyCoverter {

    public static String convertValue(float value, @ExchangeRates.RATES String fromCurrency, @ExchangeRates.RATES String toCurrency, ExchangeRates rates) throws CurrencyConverterException {
        BigDecimal valueDecimal = new BigDecimal(value);

        Double fromExchangeRate = rates.getExchangeRate(fromCurrency);
        Double toExchageRate = rates.getExchangeRate(toCurrency);

        if (fromExchangeRate == null || toExchageRate == null) {
            throw new CurrencyConverterException("one or both exchange rates are not presented in ExchangeRates object");
        }

        BigDecimal fromDecimal = new BigDecimal(fromExchangeRate);
        BigDecimal toDecimal = new BigDecimal(toExchageRate);

        int rounding = Currency.getInstance(toCurrency).getDefaultFractionDigits();

        BigDecimal result = valueDecimal.multiply(toDecimal).divide(fromDecimal, rounding, RoundingMode.HALF_DOWN);

        return CurrencyFormat.formatDecimalValue(result);
    }
}
