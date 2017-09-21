package com.rena21.driver.view.layout;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.CurrencyFormatTextView;

public class AmountSummaryLayout extends RelativeLayout{

    private CurrencyFormatTextView tvTotalSumOfOrders;
    private CurrencyFormatTextView tvTotalSumOfIncomes;
    private CurrencyFormatTextView tvTotalSumOfUnpaid;

    public AmountSummaryLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_amount_summary, this);
        tvTotalSumOfOrders = (CurrencyFormatTextView) view.findViewById(R.id.tvTotalSumOfOrders);
        tvTotalSumOfIncomes = (CurrencyFormatTextView) view.findViewById(R.id.tvTotalSumOfIncomes);
        tvTotalSumOfUnpaid = (CurrencyFormatTextView) view.findViewById(R.id.tvTotalSumOfUnpaid);
    }

    public void setTotalSumOfOrders(int sumOfTotalPrices) {
        tvTotalSumOfOrders.setWon(sumOfTotalPrices);
    }

    public void setTotalSumOfIncomes(int sumOfTotalIncomes) {
        tvTotalSumOfIncomes.setWon(sumOfTotalIncomes);
    }

    public void setTotalSumOfUnpaid(int sumOfTotalUnpaid) {
        tvTotalSumOfUnpaid.setWon(sumOfTotalUnpaid);
    }

    public void clearData() {
        tvTotalSumOfOrders.setWon(0);
        tvTotalSumOfIncomes.setWon(0);
        tvTotalSumOfUnpaid.setWon(0);
    }
}
