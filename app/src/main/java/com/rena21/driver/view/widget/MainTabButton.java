package com.rena21.driver.view.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.rena21.driver.R;


public class MainTabButton extends android.support.v7.widget.AppCompatButton {

    public MainTabButton(Context context) {
        this(context, null);
    }

    public MainTabButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.main_tab_button_background_selector);
        setPadding(0, 0, 0, 0);
        setTextColor(Color.WHITE);
    }
}
