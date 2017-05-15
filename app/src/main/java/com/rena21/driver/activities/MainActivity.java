package com.rena21.driver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.adapter.ReceivedOrdersAdapter;

public class MainActivity extends BaseActivity implements ChildEventListener{

    private RecyclerView rvReceivedOrders;
    private ReceivedOrdersAdapter receivedOrdersAdapter;

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

        rvReceivedOrders = (RecyclerView) findViewById(R.id.rvReceivedOrders);
        receivedOrdersAdapter = new ReceivedOrdersAdapter(dbManager);

        rvReceivedOrders.setLayoutManager(new LinearLayoutManager(this));
        rvReceivedOrders.addItemDecoration(new DividerItemDecoration(this, R.drawable.shape_divider_for_received_orders));
        rvReceivedOrders.setAdapter(receivedOrdersAdapter);
        receivedOrdersAdapter.addOnItemClickListener(new ReceivedOrdersAdapter.OnItemClickListener() {
            @Override public void onItemClick(String fileName, Order order) {
                goToDetailActivity(fileName);
            }
        });

        dbManager.addChildEventListenerToOrdersRef(this);
    }

    @Override protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            final String fileName = getIntent().getExtras().getString("orderKey");
            goToDetailActivity(fileName);
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

    private void goToDetailActivity(String fileName) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("vendorPhoneNumber", appPreferenceManager.getPhoneNumber());
        intent.putExtra("fileName", fileName);
        startActivity(intent);
    }
}
