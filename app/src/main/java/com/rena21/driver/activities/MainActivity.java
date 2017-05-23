package com.rena21.driver.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderClickedListener;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.fragment.LedgerFragment;
import com.rena21.driver.view.fragment.OrderListFragment;
import com.rena21.driver.viewmodel.MainTabViewModel;


public class MainActivity extends BaseActivity implements OrderClickedListener, MainTabViewModel.MainTabClickListener {

    public static final int REQUEST_CODE = 100;

    private AppPreferenceManager appPreferenceManager;
    private FirebaseDbManager dbManager;

    private MainTabViewModel mainTabViewModel;

    private OrderListFragment orderListFragment;
    private LedgerFragment ledgerFragment;

    private boolean isCallAfterDeliveryCompletion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");

        appPreferenceManager = App.getApplication(getApplicationContext()).getPreferenceManager();

        dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), appPreferenceManager.getPhoneNumber());

        orderListFragment = new OrderListFragment();
        ledgerFragment = new LedgerFragment();

        RelativeLayout mainTabContainer = (RelativeLayout) findViewById(R.id.main_tab_container);
        mainTabViewModel = new MainTabViewModel((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), mainTabContainer);
        mainTabViewModel.setMainTabClickListener(this);
    }

    @Override protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("orderKey") != null) {
            final String fileName = bundle.getString("orderKey");
            getIntent().removeExtra("orderKey");
            goToDetailActivity(fileName);
        }
    }

    @Override protected void onPostResume() {
        super.onPostResume();
        if(isCallAfterDeliveryCompletion) {
            mainTabViewModel.showLedgerTab();
        } else {
            mainTabViewModel.showOrdersTab();
        }
        isCallAfterDeliveryCompletion = false;
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE :
                if(resultCode == RESULT_OK) {
                   isCallAfterDeliveryCompletion = true;
                }
                break;
        }
    }

    @Override public void onOrderClicked(String orderKey) {
        goToDetailActivity(orderKey);
    }

    @Override public void onMainTabClick(MainTabViewModel.MainTab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (tab) {
            case TAB_1_ORDERS:
                transaction.replace(R.id.main_fragment_container, orderListFragment).commit();
                break;
            case TAB_2_LEDGER:
                transaction.replace(R.id.main_fragment_container, ledgerFragment).commit();
                break;
        }
    }

    public FirebaseDbManager getDbManager() {
        return dbManager;
    }

    private void goToDetailActivity(String fileName) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("vendorPhoneNumber", appPreferenceManager.getPhoneNumber());
        intent.putExtra("fileName", fileName);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
