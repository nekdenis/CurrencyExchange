package nekdenis.github.com.exchangerates.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.VisibleForTesting;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.ButterKnife;
import nekdenis.github.com.exchangerates.R;
import nekdenis.github.com.exchangerates.data.CurrencyObj;

/**
 * View with currency name and currency value
 * Allows to change amount of money
 */
public class ConverterView extends FrameLayout {

    @Bind(R.id.converter_currency_name)
    protected TextView currencyNameTextView;
    @Bind(R.id.converter_currency_value)
    protected EditText currencyValueEditText;

    private CurrencyValueChangeListener currencyValueChangeListener;
    private CurrencyObj currencyObj;

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
        currencyValueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String newValueString = s.toString();
                ConverterView.this.onTextChanged(newValueString);
            }
        });
    }

    private void onTextChanged(String newValueString) {
        if (currencyValueChangeListener != null && !TextUtils.isEmpty(newValueString)) {
            double newVal = Double.parseDouble(newValueString);
            currencyObj.setValue(new BigDecimal(newVal));
            currencyValueChangeListener.onValueChanged(currencyObj);
        }
    }

    private void setCurrencyName(String name) {
        currencyNameTextView.setText(name);
    }

    public String getCurrencyName() {
        return currencyNameTextView.getText().toString();
    }

    public void setCurrencyEditEnabled(boolean isEnabled) {
        currencyValueEditText.setEnabled(isEnabled);
    }

    private void setCurrencyValue(String value) {
        currencyValueEditText.setText(value);
    }

    public void setCurrencyObj(CurrencyObj currencyObj) {
        this.currencyObj = currencyObj;
        setCurrencyName(currencyObj.getName());
        setCurrencyValue(currencyObj.getValueString());
    }

    public String getCurrentCurrencyValue() {
        return currencyValueEditText.getText().toString();
    }

    public void setCurrencyValueChangeListener(CurrencyValueChangeListener currencyValueChangeListener) {
        this.currencyValueChangeListener = currencyValueChangeListener;
    }

    @VisibleForTesting
    public CurrencyValueChangeListener getCurrencyValueChangeListener() {
        return currencyValueChangeListener;
    }

    public interface CurrencyValueChangeListener {
        void onValueChanged(CurrencyObj currencyObj);
    }
}
