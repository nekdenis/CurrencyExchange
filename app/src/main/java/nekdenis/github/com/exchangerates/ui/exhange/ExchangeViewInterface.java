package nekdenis.github.com.exchangerates.ui.exhange;

import java.util.List;

import nekdenis.github.com.exchangerates.data.ExchangeRates;

public interface ExchangeViewInterface  {

    void updateCurrencyRates(ExchangeRates rates);

    void setSelectedOriginalCurrency(String selectedOriginalCurrency);

    void updateOriginalCurrencies(List<String> currencies);

    void updateConvertedCurrencies(List<String> currencies);

    void setSelectedConvertedCurrency(String selectedConvertedCurrency);

    void onRatesUpdatingError(String message);
}
