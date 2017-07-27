package com.rena21.driver.view.actionbar;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rena21.driver.R;
import com.rena21.driver.view.widget.MainTabButton;

public class ActionBarWithTabViewModel implements View.OnClickListener{

    public enum MainTab {
        TAB_1_ORDERS,
        TAB_2_LEDGER,
        TAB_3_INFO,
        TAB_4_ESTIMATE
    }

    public interface MainTabClickListener {
        void onMainTabClick(MainTab tab);
    }

    private MainTabClickListener mainTabClickListener;

    private ActionBar actionBar;

    private MainTabButton btnTabOrders;
    private MainTabButton btnTabLedger;
    private MainTabButton btnTabMyInfo;
    private MainTabButton btnTabEstimate;


    public static ActionBarWithTabViewModel createWithActionBarTab(ActionBar supportActionBar) {
        ActionBarWithTabViewModel instance = new ActionBarWithTabViewModel(supportActionBar);
        instance.setup();
        return instance;
    }

    public void setMainTabClickListener(MainTabClickListener listener) {
        this.mainTabClickListener = listener;
    }

    public void showOrdersTab() {
        onClick(btnTabOrders);
    }

    public void showLedgerTab() {
        onClick(btnTabLedger);
    }

    @Override public void onClick(View v) {
        btnTabOrders.setSelected(false);
        btnTabLedger.setSelected(false);
        btnTabMyInfo.setSelected(false);
        btnTabEstimate.setSelected(false);

        switch (v.getId()) {
            case R.id.btnTabOrders:
                mainTabClickListener.onMainTabClick(MainTab.TAB_1_ORDERS);
                btnTabOrders.setSelected(true);
                break;

            case R.id.btnTabLedger:
                mainTabClickListener.onMainTabClick(MainTab.TAB_2_LEDGER);
                btnTabLedger.setSelected(true);
                break;

            case R.id.btnTabMyInfo:
                mainTabClickListener.onMainTabClick(MainTab.TAB_3_INFO);
                btnTabMyInfo.setSelected(true);
                break;

            case R.id.btnTabEstimate:
                mainTabClickListener.onMainTabClick(MainTab.TAB_4_ESTIMATE);
                btnTabEstimate.setSelected(true);
                break;
        }
    }

    private ActionBarWithTabViewModel(ActionBar actionBar) { this.actionBar = actionBar; }

    private void setup() {
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.action_bar_with_tab);

        View tabView = actionBar.getCustomView();
        Toolbar toolbar = (Toolbar) tabView.getParent();
        toolbar.setContentInsetsAbsolute(0,0);

        btnTabOrders = (MainTabButton) tabView.findViewById(R.id.btnTabOrders);
        btnTabLedger = (MainTabButton) tabView.findViewById(R.id.btnTabLedger);
        btnTabMyInfo = (MainTabButton) tabView.findViewById(R.id.btnTabMyInfo);
        btnTabEstimate = (MainTabButton) tabView.findViewById(R.id.btnTabEstimate);

        btnTabOrders.setOnClickListener(this);
        btnTabLedger.setOnClickListener(this);
        btnTabMyInfo.setOnClickListener(this);
        btnTabEstimate.setOnClickListener(this);
    }
}
