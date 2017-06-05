package com.rena21.driver.view.widget;


import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;

public class CurrencyFormatEditText extends android.support.v7.widget.AppCompatEditText {

    public interface AmountInputFinishListener {
        void onAmountInputFinish(long amount);
    }

    private AmountInputFinishListener listener;

    public CurrencyFormatEditText(Context context) {
        super(context, null);
    }

    public CurrencyFormatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setInputType(InputType.TYPE_CLASS_NUMBER);

        addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override public void afterTextChanged(Editable s) {
                removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    long longVal;

                    originalString = originalString.replaceAll(",", "");

                    longVal = Long.parseLong(originalString);

                    setText(String.format("%,d", longVal));
                    setSelection(getText().length());
                } catch (NumberFormatException e) {}

                if(listener!= null) listener.onAmountInputFinish(getAmount());

                addTextChangedListener(this);
            }
        });
    }

    public void addTextChangedFinishListener(AmountInputFinishListener listener) {
        this.listener = listener;
    }

    public long getAmount() {
        return getText().toString().equals("") ? 0 : Long.parseLong(getText().toString().replaceAll(",", ""));
    }
}
