package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderAcceptedListener;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.fragment.OrderDetailFragment;

import java.util.HashMap;


public class OrderDetailActivity extends BaseActivity implements OrderAcceptedListener{

    private OrderDetailFragment orderDetailFragment;

    private FirebaseDbManager dbManager;

    private String vendorPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("납품 상세");

        vendorPhoneNumber = getIntent().getStringExtra("vendorPhoneNumber");

        dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), vendorPhoneNumber);

        orderDetailFragment = new OrderDetailFragment();
        orderDetailFragment.setArguments(getIntent().getExtras());

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.order_detail_fragment_container, orderDetailFragment).commit();
    }

    @Override public void onOrderAccepted(String fileName) {
        HashMap<String, Object> pathMap = new HashMap<>();
        // 확인 버튼을 눌렀는지 저장하기 위한 경로
        pathMap.put("/orders/vendors/" + vendorPhoneNumber + "/" + fileName + "/accepted/", true);
        // 식당앱을 위한 경로
        pathMap.put("/orders/restaurants/" + fileName + "/" + vendorPhoneNumber + "/accepted/", true);

        dbManager.multiPathUpdateValue(pathMap, new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e("error", task.getResult().toString());
                }
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(orderDetailFragment).commit();
        finish();
    }

    public FirebaseDbManager getDbManager() {
        return dbManager;
    }
}
