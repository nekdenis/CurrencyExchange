package nekdenis.github.com.exchangerates.ui.exhange;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nekdenis.github.com.exchangerates.R;
import nekdenis.github.com.exchangerates.data.ExchangeRates;
import nekdenis.github.com.exchangerates.ui.exhange.adapter.ExchangeConvertedViewPagerAdapter;
import nekdenis.github.com.exchangerates.ui.exhange.adapter.ExchangeOriginalViewPagerAdapter;

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
    }

    private void initViewPagers() {
        originalPagerAdapter = new ExchangeOriginalViewPagerAdapter(this);
        originalCurrenciesViewPager.setAdapter(originalPagerAdapter);
        convertedPagerAdapter = new ExchangeConvertedViewPagerAdapter(this);
        convertedCurrenciesViewPager.setAdapter(convertedPagerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachViewInterface(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachViewInterface();
    }

    @Override
    public void updateCurrencyRates(ExchangeRates rates) {
        String selectedItem = getSelectedCurrency();
        convertedPagerAdapter.onRateChanged(rates, selectedItem, 10f);
    }

    @Override
    public void setSelectedOriginalCurrency(String selectedOriginalCurrency) {

    }

    @Override
    public void updateOriginalCurrencies(List<String> currencies) {
        originalPagerAdapter.setData(currencies);
    }

    @Override
    public void updateConvertedCurrencies(List<String> currencies) {
        convertedPagerAdapter.setData(currencies);
    }

    @Override
    public void setSelectedConvertedCurrency(String selectedConvertedCurrency) {

    }

    @Override
    public void onRatesUpdatingError(String message) {
        Snackbar.make(convertedCurrenciesViewPager, message, Snackbar.LENGTH_SHORT).show();
    }

    private String getSelectedCurrency() {
        return originalPagerAdapter.getItemAt(originalCurrenciesViewPager.getCurrentItem());
    }
}
