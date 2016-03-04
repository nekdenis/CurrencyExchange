package nekdenis.github.com.exchangerates.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import nekdenis.github.com.exchangerates.R;

public class ConverterView extends FrameLayout {

    @Bind(R.id.converter_currency_name)
    protected TextView currencyNameTextView;
    @Bind(R.id.converter_currency_value)
    protected TextView currencyValueEditText;

    public ConverterView(Context context) {
        super(context);
        initView();
    }

    public ConverterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ConverterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConverterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_converter, this);
        ButterKnife.bind(this);
    }

    public void setCurrencyName(String name) {
        currencyNameTextView.setText(name);
    }

    public String getCurrencyName() {
        return currencyNameTextView.getText().toString();
    }

    public void setCurrencyEditEnabled(boolean isEnabled) {
        currencyValueEditText.setEnabled(isEnabled);
    }

    public void setCurrencyValue(String value) {
        currencyValueEditText.setText(value);
    }

    public String getCurrentCurrencyValue() {
        return currencyValueEditText.getText().toString();
    }
}
