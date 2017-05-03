package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.listener.ReceivedOrderTouchListener;
import com.rena21.driver.pojo.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.adapter.ReceivedOrdersAdapter;

public class MainActivity extends BaseActivity {

    private RecyclerView rvReceivedOrders;
    private ReceivedOrdersAdapter receivedOrdersAdapter;

    private DatabaseReference vendorRef;
    private ChildEventListener receivedOrderEventListener;

    private AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");

        rvReceivedOrders = (RecyclerView) findViewById(R.id.rvReceivedOrders);
        receivedOrdersAdapter = new ReceivedOrdersAdapter();

        rvReceivedOrders.setLayoutManager(new LinearLayoutManager(this));
        rvReceivedOrders.addItemDecoration(new DividerItemDecoration(this, R.drawable.shape_divider_for_received_orders));
        rvReceivedOrders.setAdapter(receivedOrdersAdapter);
        rvReceivedOrders.addOnItemTouchListener(new ReceivedOrderTouchListener(this, new ReceivedOrderTouchListener.ItemClickListener() {
            @Override public void onItemClick(View childView) {
                int position = rvReceivedOrders.getChildAdapterPosition(childView);
                Log.d("MainActivity", "item click : " + position + "번째");

                Order order = receivedOrdersAdapter.getItem(position);
                Log.d("MainActivity", "restaurant name : " + order.restaurantName + " " + "order items :" + order.orderItems);
            }
        }));

        appPreferenceManager = App.getApplication(getApplicationContext()).getPreferenceManager();

        vendorRef = FirebaseDatabase.getInstance()
                .getReference("vendors")
                .child(appPreferenceManager.getPhoneNumber());

        vendorRef.keepSynced(true);

        receivedOrderEventListener = new ChildEventListener() {
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
        };
    }

    @Override protected void onStart() {
        super.onStart();
        vendorRef.child("receivedOrders").addChildEventListener(receivedOrderEventListener);
    }

    @Override protected void onStop() {
        super.onStop();
        vendorRef.removeEventListener(receivedOrderEventListener);
    }
}
