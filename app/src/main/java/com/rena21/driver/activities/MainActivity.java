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
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.fragment.OrderListFragment;

public class MainActivity extends BaseActivity implements OrderClickedListener {

    private AppPreferenceManager appPreferenceManager;
    private FirebaseDbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");

        appPreferenceManager = App.getApplication(getApplicationContext()).getPreferenceManager();

        dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), appPreferenceManager.getPhoneNumber());

        OrderListFragment orderListFragment = new OrderListFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, orderListFragment).commit();
    }

    @Override protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            final String fileName = getIntent().getExtras().getString("orderKey");
            goToDetailActivity(fileName);
        }
    }

    @Override public void onOrderClicked(String orderKey) {
        goToDetailActivity(orderKey);
    }

    public FirebaseDbManager getDbManager() {
        return dbManager;
    }

    private void goToDetailActivity(String fileName) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("vendorPhoneNumber", appPreferenceManager.getPhoneNumber());
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }
}
