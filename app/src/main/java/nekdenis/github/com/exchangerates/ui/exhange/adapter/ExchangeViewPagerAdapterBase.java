package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.ui.view.ConverterView;

public abstract class ExchangeViewPagerAdapterBase extends PagerAdapter {

    public static final String CURRENCY_UNKNOWN_VALUE = "-";
    protected Context context;
    protected List<String> currencies;
    protected boolean isEditableValues;
    protected ViewGroup container;

    public ExchangeViewPagerAdapterBase(boolean isEditableValues, Context context) {
        this.isEditableValues = isEditableValues;
        this.context = context;
        currencies = new ArrayList<>();
    }

    public void setData(List<String> newCurrencies) {
        currencies.clear();
        currencies.addAll(newCurrencies);
        notifyDataSetChanged();
    }

    public String getItemAt(int index) {
        return currencies.get(index);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.container = container;
        String currencyName = currencies.get(position);
        String exchangeRateValue = getCurrencyValue(currencyName);
        ConverterView view = getConverterView(currencyName, exchangeRateValue);
        container.addView(view);
        return view;
    }

    abstract String getCurrencyValue(String currencyName);

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @NonNull
    ConverterView getConverterView(String currency, String exchangeRateValue) {
        ConverterView view = new ConverterView(context);
        view.setCurrencyName(currency);
        view.setCurrencyEditEnabled(isEditableValues);
        view.setCurrencyValue(exchangeRateValue);
        return view;
    }
}
