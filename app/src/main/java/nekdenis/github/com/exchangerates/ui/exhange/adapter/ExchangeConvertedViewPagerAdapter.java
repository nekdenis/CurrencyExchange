package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.content.Context;

import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.ui.view.ConverterView;
import nekdenis.github.com.exchangerates.util.money.CurrencyConverterException;
import nekdenis.github.com.exchangerates.util.money.CurrencyCoverter;

public class ExchangeConvertedViewPagerAdapter extends ExchangeViewPagerAdapterBase {

    ExchangeRates rates;

    public ExchangeConvertedViewPagerAdapter(Context context) {
        super(false, context);
    }

    public void onRateChanged(ExchangeRates rates, String selectedCurrency, Float selectedValue) {
        this.rates = rates;
        for (int i = 0; i < container.getChildCount(); i++) {
            updateExchangeRateValue(((ConverterView) container.getChildAt(i)), selectedCurrency, selectedValue);
        }
    }

    @Override
    String getCurrencyValue(String currencyName) {
        return "10";
    }

    void updateExchangeRateValue(ConverterView converterView, String selectedCurrency, Float selectedValue) {
        String value = null;
        try {
            //noinspection ResourceType
            value = CurrencyCoverter.convertValue(selectedValue, selectedCurrency, converterView.getCurrencyName(), rates);
        } catch (CurrencyConverterException e) {
            value = CURRENCY_UNKNOWN_VALUE;
        }
        converterView.setCurrencyValue(value);
    }
}
