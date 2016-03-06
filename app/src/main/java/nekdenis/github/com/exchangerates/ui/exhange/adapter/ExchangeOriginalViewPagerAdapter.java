package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.ui.view.ConverterView;

/**
 * View Adapter for original currency converter views
 */
public class ExchangeOriginalViewPagerAdapter extends ExchangeViewPagerAdapterBase {

    private ConverterView.CurrencyValueChangeListener currencyValueChangeListener;

    public ExchangeOriginalViewPagerAdapter(Context context, ConverterView.CurrencyValueChangeListener currencyValueChangeListener) {
        super(true, context);
        this.currencyValueChangeListener = currencyValueChangeListener;
    }

    @NonNull
    @Override
    ConverterView createConverterView(CurrencyObj currency) {
        ConverterView converterView = super.createConverterView(currency);
        converterView.setCurrencyValueChangeListener(currencyValueChangeListener);
        return converterView;
    }
}
