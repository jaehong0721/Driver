package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.listener.OrderAcceptedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.adapter.ReceivedOrdersAdapter;
import com.rena21.driver.view.fragment.OrderDetailDialogFragment;

import java.util.HashMap;

public class MainActivity extends BaseActivity implements OrderAcceptedListener, ChildEventListener{

    private RecyclerView rvReceivedOrders;
    private ReceivedOrdersAdapter receivedOrdersAdapter;

    private OrderDetailDialogFragment orderDetailDialogFragment;

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

        orderDetailDialogFragment = new OrderDetailDialogFragment();

        rvReceivedOrders = (RecyclerView) findViewById(R.id.rvReceivedOrders);
        receivedOrdersAdapter = new ReceivedOrdersAdapter(dbManager);

        rvReceivedOrders.setLayoutManager(new LinearLayoutManager(this));
        rvReceivedOrders.addItemDecoration(new DividerItemDecoration(this, R.drawable.shape_divider_for_received_orders));
        rvReceivedOrders.setAdapter(receivedOrdersAdapter);
        receivedOrdersAdapter.addOnItemClickListener(new ReceivedOrdersAdapter.OnItemClickListener() {
            @Override public void onItemClick(String fileName, Order order) {
                Bundle bundle = new Bundle();
                bundle.putString("fileName", fileName);
                orderDetailDialogFragment.setArguments(bundle);
                orderDetailDialogFragment.show(getSupportFragmentManager(), "order_detail");
            }
        });

        dbManager.addChildEventListenerToOrdersRef(this);
    }

    @Override protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            final String fileName = getIntent().getExtras().getString("orderKey");
            Bundle bundle = new Bundle();
            bundle.putString("fileName", fileName);
            orderDetailDialogFragment.setArguments(bundle);
            orderDetailDialogFragment.show(getSupportFragmentManager(), "order_detail");
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        dbManager.removeChildEventListenerFromOrderRef(this);
    }

    @Override public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        String fileName = dataSnapshot.getKey();
        Order order = dataSnapshot.getValue(Order.class);

        receivedOrdersAdapter.addedItem(fileName, order);
    }

    @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        String fileName = dataSnapshot.getKey();
        Order order = dataSnapshot.getValue(Order.class);

        receivedOrdersAdapter.changedItem(fileName, order);
    }

    @Override public void onChildRemoved(DataSnapshot dataSnapshot) {
        String fileName = dataSnapshot.getKey();

        receivedOrdersAdapter.removedItem(fileName);
    }

    @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

    @Override public void onCancelled(DatabaseError databaseError) {}

    @Override public void onOrderAccepted(String fileName) {
        HashMap<String, Object> pathMap = new HashMap<>();
        // 확인 버튼을 눌렀는지 저장하기 위한 경로
        pathMap.put("/orders/vendors/" + appPreferenceManager.getPhoneNumber() + "/" + fileName + "/accepted/", true);
        // 식당앱을 위한 경로
        pathMap.put("/orders/restaurants/" + fileName + "/" + appPreferenceManager.getPhoneNumber() + "/accepted/", true);

        dbManager.multiPathUpdateValue(pathMap, new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful()) {
                    Log.e("error", task.getResult().toString());
                }
            }
        });
    }

    public FirebaseDbManager getDbManager() {
        return dbManager;
    }
}
