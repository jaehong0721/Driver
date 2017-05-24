package com.rena21.driver.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.rena21.driver.R;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.layout.DeliveryOrderTabsLayout;


public class DeliveryDetailActivity extends BaseActivity implements DeliveryOrderTabsLayout.DeliveryOrderTabClickListener{

    private TextView tvRestaurantName;
    private DeliveryOrderTabsLayout tabsLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("납품상세");

        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        tabsLayout = (DeliveryOrderTabsLayout) findViewById(R.id.deliveryDetailTab);

        tabsLayout.setTabClickListener(this);
        tabsLayout.showInitialView();
    }

    @Override public void onClickTab(DeliveryOrderTabsLayout.DeliveryOrderTab tab) {
        switch (tab) {
            case TAB_1_ORDER_DETAILS:
                Toast.makeText(this, "detail", Toast.LENGTH_SHORT).show();
                break;

            case TAB_2_MODIFY:
                Toast.makeText(this, "modify", Toast.LENGTH_SHORT).show();
                break;

            case TAB_3_BILLS:
                Toast.makeText(this, "bills", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
