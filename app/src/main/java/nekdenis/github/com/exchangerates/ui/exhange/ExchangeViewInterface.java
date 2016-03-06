package nekdenis.github.com.exchangerates.ui.exhange;

import java.util.List;

import nekdenis.github.com.exchangerates.data.CurrencyObj;

public interface ExchangeViewInterface  {

    void updateOriginalCurrencies(List<CurrencyObj> currencies);

    void updateConvertedCurrencies(List<CurrencyObj> currencies);

    void onRatesUpdatingError(String message);
}
