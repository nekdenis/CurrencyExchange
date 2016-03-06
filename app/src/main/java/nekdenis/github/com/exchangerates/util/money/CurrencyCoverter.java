package nekdenis.github.com.exchangerates.util.money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public class CurrencyCoverter {

    /**
     *
     * Convert value to other currency
     *
     * @param value amount of money in fromCurrency
     * @param fromCurrency currency of input amount
     * @param toCurrency currency of output amount
     * @param rates all available convert rates
     * @return converted value in toCurrency currency
     *
     * @throws CurrencyConverterException
     */
    public static BigDecimal convertValue(BigDecimal value, @ExchangeRates.RATES String fromCurrency, @ExchangeRates.RATES String toCurrency, ExchangeRates rates) throws CurrencyConverterException {
        BigDecimal valueDecimal = value;

        Double fromExchangeRate = rates.getExchangeRate(fromCurrency);
        Double toExchageRate = rates.getExchangeRate(toCurrency);

        if (fromExchangeRate == null || toExchageRate == null) {
            throw new CurrencyConverterException("one or both exchange rates are not presented in ExchangeRates object");
        }

        BigDecimal fromDecimal = new BigDecimal(fromExchangeRate);
        BigDecimal toDecimal = new BigDecimal(toExchageRate);

        int rounding = Currency.getInstance(toCurrency).getDefaultFractionDigits();

        BigDecimal result = valueDecimal.multiply(toDecimal).divide(fromDecimal, rounding, RoundingMode.HALF_DOWN);

        return result;
    }
}
