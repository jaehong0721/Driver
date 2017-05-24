package com.rena21.driver.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.ModifyFinishedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.fragment.DeliveryDetailFragment;
import com.rena21.driver.view.fragment.DeliveryModifyFragment;
import com.rena21.driver.view.layout.DeliveryOrderTabsLayout;

import java.util.HashMap;


public class DeliveryDetailActivity extends BaseActivity implements DeliveryOrderTabsLayout.DeliveryOrderTabClickListener,
                                                                    ModifyFinishedListener {

    private TextView tvRestaurantName;
    private DeliveryOrderTabsLayout tabsLayout;

    private FirebaseDbManager dbManager;

    private String fileName;
    private String vendorPhoneNumber;

    private DeliveryDetailFragment deliveryDetailFragment;
    private DeliveryModifyFragment deliveryModifyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_detail);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("납품상세");

        fileName = getIntent().getStringExtra("fileName");
        vendorPhoneNumber = getIntent().getStringExtra("vendorPhoneNumber");

        dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), vendorPhoneNumber);

        tvRestaurantName = (TextView) findViewById(R.id.tvRestaurantName);
        final String restaurantPhoneNumber = getPhoneNumber(fileName);
        dbManager.getRestaurantName(restaurantPhoneNumber, new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                String restaurantName = dataSnapshot.getValue(String.class);
                if (restaurantName == null) {
                    tvRestaurantName.setText(restaurantPhoneNumber);
                } else {
                    tvRestaurantName.setText(restaurantName);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) { }
        });

        deliveryDetailFragment = DeliveryDetailFragment.newInstance(fileName);
        deliveryModifyFragment = DeliveryModifyFragment.newInstance(fileName);

        tabsLayout = (DeliveryOrderTabsLayout) findViewById(R.id.deliveryDetailTab);
        tabsLayout.setTabClickListener(this);
        tabsLayout.showInitialView();
    }

    @Override public void onClickTab(DeliveryOrderTabsLayout.DeliveryOrderTab tab) {

        hideKeyboard();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        switch (tab) {
            case TAB_1_ORDER_DETAILS:
                ft.replace(R.id.delivery_detail_fragment_container, deliveryDetailFragment).commit();
                break;

            case TAB_2_MODIFY:
                ft.replace(R.id.delivery_detail_fragment_container, deliveryModifyFragment).commit();
                break;

            case TAB_3_BILLS:
                Toast.makeText(this, "bills", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override public void onModifyFinished(Order order, String fileName) {
        HashMap<String, Object> pathMap = new HashMap<>();
        pathMap.put("/orders/vendors/" + vendorPhoneNumber + "/" + fileName + "/", order);
        pathMap.put("/orders/restaurants/" + fileName + "/" + vendorPhoneNumber + "/", order);

        dbManager.multiPathUpdateValue(pathMap, new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e("error", task.getResult().toString());
                }
            }
        });

        tabsLayout.showInitialView();
    }

    private String getPhoneNumber(String fileName) {
        return fileName.split("_")[0];
    }

    public FirebaseDbManager getDbManager() {
        return dbManager;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
