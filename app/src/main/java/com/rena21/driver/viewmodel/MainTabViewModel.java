package com.rena21.driver.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.MainTabButton;


public class MainTabViewModel implements View.OnClickListener {

    public enum MainTab {
        TAB_1_ORDERS,
        TAB_2_LEDGER,
    }

    public interface MainTabClickListener {
        void onMainTabClick(MainTab tab);
    }

    private MainTabClickListener mainTabClickListener;

    private MainTabButton btnTab1Orders;
    private MainTabButton btnTab2Ledger;

    public MainTabViewModel(LayoutInflater layoutInflater, ViewGroup rootView) {
        View view = layoutInflater.inflate(R.layout.layout_main_tabs, rootView, true);
        btnTab1Orders = (MainTabButton) view.findViewById(R.id.btnTabOrders);
        btnTab2Ledger = (MainTabButton) view.findViewById(R.id.btnTabLedger);

        btnTab1Orders.setOnClickListener(this);
        btnTab2Ledger.setOnClickListener(this);
    }

    public void setMainTabClickListener(MainTabClickListener listener) {
        this.mainTabClickListener = listener;
    }

    public void showInitialTab() {
        onClick(btnTab1Orders);
    }

    @Override public void onClick(View v) {
        btnTab1Orders.setSelected(false);
        btnTab2Ledger.setSelected(false);

        switch (v.getId()) {
            case R.id.btnTabOrders:
                mainTabClickListener.onMainTabClick(MainTab.TAB_1_ORDERS);
                btnTab1Orders.setSelected(true);
//                btnTab1Orders.setClickable(false);
//                btnTab2Ledger.setClickable(true);
                break;
            case R.id.btnTabLedger:
                mainTabClickListener.onMainTabClick(MainTab.TAB_2_LEDGER);
                btnTab2Ledger.setSelected(true);
//                btnTab2Ledger.setClickable(false);
//                btnTab1Orders.setClickable(true);
                break;
        }
    }

}
