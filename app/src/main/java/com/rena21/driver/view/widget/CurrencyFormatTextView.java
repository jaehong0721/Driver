package com.rena21.driver.view.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class CurrencyFormatTextView extends android.support.v7.widget.AppCompatTextView{

    public CurrencyFormatTextView(Context context) {
        super(context, null);
    }

    public CurrencyFormatTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setWon(long amount) {
        super.setText(String.format("%,d", amount) + "원");
    }
}
