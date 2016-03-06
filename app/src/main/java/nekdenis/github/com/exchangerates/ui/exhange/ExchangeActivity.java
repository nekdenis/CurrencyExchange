package nekdenis.github.com.exchangerates.ui.exhange;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nekdenis.github.com.exchangerates.R;
import nekdenis.github.com.exchangerates.data.CurrencyObj;
import nekdenis.github.com.exchangerates.ui.exhange.adapter.ExchangeConvertedViewPagerAdapter;
import nekdenis.github.com.exchangerates.ui.exhange.adapter.ExchangeOriginalViewPagerAdapter;
import nekdenis.github.com.exchangerates.ui.view.ConverterView;

public class ExchangeActivity extends AppCompatActivity implements ExchangeViewInterface {

    @Bind(R.id.exchange_original_viewpager)
    protected ViewPager originalCurrenciesViewPager;
    @Bind(R.id.exchange_converted_viewpager)
    protected ViewPager convertedCurrenciesViewPager;

    private ExchangePresenter presenter;
    private ExchangeOriginalViewPagerAdapter originalPagerAdapter;
    private ExchangeConvertedViewPagerAdapter convertedPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.bind(this);
        presenter = new ExchangePresenter();

        initViewPagers();

        presenter.attachViewInterface(this);
        if (savedInstanceState != null) {
            presenter.onRestoreInstanceState(savedInstanceState);
        }
    }

    private void initViewPagers() {
        originalPagerAdapter = new ExchangeOriginalViewPagerAdapter(this, currencyValueChangeListener);
        originalCurrenciesViewPager.setAdapter(originalPagerAdapter);
        originalCurrenciesViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                CurrencyObj selectedCurrency = originalPagerAdapter.getItemAt(position);
                presenter.onSelectedOriginalCurrencyChanged(selectedCurrency);
            }
        });
        convertedPagerAdapter = new ExchangeConvertedViewPagerAdapter(this);
        convertedCurrenciesViewPager.setAdapter(convertedPagerAdapter);
        convertedCurrenciesViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                CurrencyObj selectedCurrency = convertedPagerAdapter.getItemAt(position);
                presenter.onSelectedConvertedCurrencyChanged(selectedCurrency);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachViewInterface();
    }

    @Override
    public void updateOriginalCurrencies(List<CurrencyObj> currencies) {
        originalPagerAdapter.setData(currencies);
    }

    @Override
    public void updateConvertedCurrencies(List<CurrencyObj> currencies) {
        convertedPagerAdapter.setData(currencies);
    }

    @Override
    public void onRatesUpdatingError(String message) {
        Snackbar.make(convertedCurrenciesViewPager, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void notifyRatesUpdated() {
        Snackbar.make(convertedCurrenciesViewPager, R.string.exchange_rates_updated, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void selectCurrencies(CurrencyObj originalSelectedCurrency, CurrencyObj convertedSelectedCurrency) {

    }

    private ConverterView.CurrencyValueChangeListener currencyValueChangeListener = new ConverterView.CurrencyValueChangeListener() {
        @Override
        public void onValueChanged(CurrencyObj currencyObj) {
            presenter.onRateChanged(currencyObj);
        }
    };
}
