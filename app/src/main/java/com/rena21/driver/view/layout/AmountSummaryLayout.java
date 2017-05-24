package com.rena21.driver.view.layout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rena21.driver.R;

public class AmountSummaryLayout extends RelativeLayout{

    private TextView tvTotalSumOfOrders;

    public AmountSummaryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_amount_summary, this);
        tvTotalSumOfOrders = (TextView) view.findViewById(R.id.tvTotalSumOfOrders);
    }

    public void setTotalSumOfOrders(int sumOfTotalPrices) {
        tvTotalSumOfOrders.setText(sumOfTotalPrices + "원");
    }

    public void clearData() {
        tvTotalSumOfOrders.setText("0원");
    }
}
