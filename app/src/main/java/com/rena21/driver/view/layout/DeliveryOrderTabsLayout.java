package com.rena21.driver.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.UnderlineTabButton;


public class DeliveryOrderTabsLayout extends FrameLayout implements View.OnClickListener{

    public enum DeliveryOrderTab {
        TAB_1_ORDER_DETAILS,
        TAB_2_MODIFY,
        TAB_3_BILLS
    }

    public interface DeliveryOrderTabClickListener {
        void onClickTab(DeliveryOrderTab tab);
    }

    private DeliveryOrderTabClickListener tabListener;

    private UnderlineTabButton btnOrderDetails;
    private UnderlineTabButton btnModify;
    private UnderlineTabButton btnBills;

    public DeliveryOrderTabsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = inflate(context, R.layout.layout_delivery_order_tabs, this);
        btnOrderDetails = (UnderlineTabButton) view.findViewById(R.id.btnOrderDetails);
        btnModify = (UnderlineTabButton) view.findViewById(R.id.btnModify);
        btnBills = (UnderlineTabButton) view.findViewById(R.id.btnBills);

        btnOrderDetails.setText("납품내역");
        btnModify.setText("납품수정");
        btnBills.setText("결제/수금");

        btnOrderDetails.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnBills.setOnClickListener(this);
    }

    @Override public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOrderDetails:
                tabListener.onClickTab(DeliveryOrderTab.TAB_1_ORDER_DETAILS);
                btnOrderDetails.setSelected(true);
                btnModify.setSelected(false);
                btnBills.setSelected(false);
                break;

            case R.id.btnModify:
                tabListener.onClickTab(DeliveryOrderTab.TAB_2_MODIFY);
                btnModify.setSelected(true);
                btnOrderDetails.setSelected(false);
                btnBills.setSelected(false);
                break;

            case R.id.btnBills:
                tabListener.onClickTab(DeliveryOrderTab.TAB_3_BILLS);
                btnBills.setSelected(true);
                btnOrderDetails.setSelected(false);
                btnModify.setSelected(false);
                break;
        }
    }

    public void setTabClickListener(DeliveryOrderTabClickListener tabListener) {
        this.tabListener = tabListener;
    }

    public void showInitialView() {
        btnOrderDetails.callOnClick();
    }
}
