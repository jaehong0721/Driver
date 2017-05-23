package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderAcceptedListener;
import com.rena21.driver.listener.OrderDeliveryFinishedListener;
import com.rena21.driver.models.OrderItem;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.fragment.OrderAcceptFragment;
import com.rena21.driver.view.fragment.OrderDeliveryFinishFragment;

import java.util.HashMap;
import java.util.List;


public class OrderDetailActivity extends BaseActivity implements OrderAcceptedListener, OrderDeliveryFinishedListener {

    private FirebaseDbManager dbManager;
    private String vendorPhoneNumber;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("주문 상세");

        vendorPhoneNumber = getIntent().getStringExtra("vendorPhoneNumber");
        fileName = getIntent().getStringExtra("fileName");

        dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), vendorPhoneNumber);
        dbManager.getOrderAccepted(fileName, new ValueEventListener() {

            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean accepted = dataSnapshot.getValue(Boolean.class);
                if(accepted == null) {
                    FragmentTransaction openOrderAcceptFragment = getSupportFragmentManager().beginTransaction();
                    openOrderAcceptFragment
                            .add(R.id.order_detail_fragment_container, OrderAcceptFragment.newInstance(fileName))
                            .commit();
                } else {
                    FragmentTransaction openOrderDeliveryFinishFragment = getSupportFragmentManager().beginTransaction();
                    openOrderDeliveryFinishFragment
                            .add(R.id.order_detail_fragment_container, OrderDeliveryFinishFragment.newInstance(fileName))
                            .commit();
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public FirebaseDbManager getDbManager() {
        return dbManager;
    }

    @Override public void onOrderAccepted(String fileName) {
        HashMap<String, Object> pathMap = new HashMap<>();
        pathMap.put("/orders/vendors/" + vendorPhoneNumber + "/" + fileName + "/accepted/", true);
        pathMap.put("/orders/restaurants/" + fileName + "/" + vendorPhoneNumber + "/accepted/", true);

        dbManager.multiPathUpdateValue(pathMap, new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e("error", task.getResult().toString());
                }
            }
        });

        finish();
    }

    @Override public void onOrderDeliveryFinished(List<OrderItem> orderItems, String fileName) {
        HashMap<String, Object> pathMap = new HashMap<>();
        pathMap.put("/orders/vendors/" + vendorPhoneNumber + "/" + fileName + "/delivered/", true);
        pathMap.put("/orders/vendors/" + vendorPhoneNumber + "/" + fileName + "/orderItems/", orderItems);

        pathMap.put("/orders/restaurants/" + fileName + "/" + vendorPhoneNumber + "/delivered/", true);
        pathMap.put("/orders/restaurants/" + fileName + "/" + vendorPhoneNumber + "/orderItems/", orderItems);

        dbManager.multiPathUpdateValue(pathMap, new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e("error", task.getResult().toString());
                }
            }
        });

        setResult(RESULT_OK);
        finish();
    }
}
