package nekdenis.github.com.exchangerates.ui.exhange.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.ui.view.ConverterView;

public abstract class ExchangeViewPagerAdapterBase extends PagerAdapter {

    protected Context context;
    protected List<CurrencyObj> currencies;
    protected boolean isEditableValues;
    protected ViewGroup container;

    public ExchangeViewPagerAdapterBase(boolean isEditableValues, Context context) {
        this.isEditableValues = isEditableValues;
        this.context = context;
        currencies = new ArrayList<>();
    }

    public void setData(List<CurrencyObj> newCurrencies) {
        currencies.clear();
        currencies.addAll(newCurrencies);
        notifyDataSetChanged();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public CurrencyObj getItemAt(int index) {
        return currencies.get(index);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        this.container = container;
        ConverterView view = createConverterView(position);
        container.addView(view);
        return view;
    }

    @NonNull
    protected ConverterView createConverterView(int position) {
        CurrencyObj currency = currencies.get(position);
        return createConverterView(currency);
    }

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
    ConverterView createConverterView(CurrencyObj currency) {
        ConverterView view = new ConverterView(context);
        view.setCurrencyEditEnabled(isEditableValues);
        view.setCurrencyObj(currency);
        return view;
    }

    public List<CurrencyObj> getData() {
        return currencies;
    }
}
