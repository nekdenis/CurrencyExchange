package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.content.Context;

public class ExchangeOriginalViewPagerAdapter extends ExchangeViewPagerAdapterBase {

    public ExchangeOriginalViewPagerAdapter(Context context) {
        super(true, context);
    }

    @Override
    String getCurrencyValue(String currencyName) {
        return "10";
    }
}
