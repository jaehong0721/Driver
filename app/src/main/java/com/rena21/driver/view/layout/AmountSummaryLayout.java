package com.rena21.driver.view.layout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rena21.driver.R;

public class AmountSummaryLayout extends RelativeLayout{

    public AmountSummaryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_amount_summary, this);
    }
}
