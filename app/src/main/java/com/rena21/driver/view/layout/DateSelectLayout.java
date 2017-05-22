package com.rena21.driver.view.layout;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.rena21.driver.R;

public class DateSelectLayout extends RelativeLayout {

    public DateSelectLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.layout_date_select, this);
    }
}
