package com.rena21.driver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderClickedListener;
import com.rena21.driver.view.actionbar.ActionBarWithTabViewModel;
import com.rena21.driver.view.fragment.EstimateFragment;
import com.rena21.driver.view.fragment.LedgerFragment;
import com.rena21.driver.view.fragment.MyInfoFragment;
import com.rena21.driver.view.fragment.OrderListFragment;


public class MainActivity extends BaseActivity implements OrderClickedListener, ActionBarWithTabViewModel.MainTabClickListener {

    public static final int REQUEST_CODE = 100;

    private AppPreferenceManager appPreferenceManager;
    private FirebaseDbManager dbManager;

    private ActionBarWithTabViewModel mainTab;

    private OrderListFragment orderListFragment;
    private LedgerFragment ledgerFragment;
    private MyInfoFragment myInfoFragment;
    private EstimateFragment estimateFragment;

    private boolean isCallAfterDeliveryCompletion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTab = ActionBarWithTabViewModel.createWithActionBarTab(getSupportActionBar());

        appPreferenceManager = App.getApplication(getApplicationContext()).getPreferenceManager();

        dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), appPreferenceManager.getPhoneNumber());

        orderListFragment = new OrderListFragment();
        ledgerFragment = new LedgerFragment();
        myInfoFragment = MyInfoFragment.newInstance(appPreferenceManager.getPhoneNumber());
        estimateFragment = new EstimateFragment();

        mainTab.setMainTabClickListener(this);
        mainTab.showOrdersTab();
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
            mainTab.showLedgerTab();
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

            default:
                super.onActivityResult(requestCode,resultCode,data);
                break;
        }
    }

    @Override public void onOrderClicked(String from, String orderKey) {
        switch (from) {
            case "detail":
                goToDetailActivity(orderKey);
                break;

            case "ledger":
                goToDeliveryDetailActivity(orderKey);
                break;
        }
    }

    @Override public void onMainTabClick(ActionBarWithTabViewModel.MainTab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (tab) {
            case TAB_1_ORDERS:
                transaction.replace(R.id.main_fragment_container, orderListFragment).commit();
                break;
            case TAB_2_LEDGER:
                transaction.replace(R.id.main_fragment_container, ledgerFragment).commit();
                break;
            case TAB_3_INFO:
                transaction.replace(R.id.main_fragment_container, myInfoFragment).commit();
                break;
            case TAB_4_ESTIMATE:
                transaction.replace(R.id.main_fragment_container, estimateFragment).commit();
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

    private void goToDeliveryDetailActivity(String fileName) {
        Intent intent = new Intent(this, DeliveryDetailActivity.class);
        intent.putExtra("vendorPhoneNumber", appPreferenceManager.getPhoneNumber());
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }
}
