package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.listener.OrderAcceptedListener;
import com.rena21.driver.models.Order;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.adapter.ReceivedOrdersAdapter;
import com.rena21.driver.view.fragment.OrderDetailDialogFragment;

import java.util.HashMap;

public class MainActivity extends BaseActivity implements OrderAcceptedListener {

    private RecyclerView rvReceivedOrders;
    private ReceivedOrdersAdapter receivedOrdersAdapter;

    private OrderDetailDialogFragment orderDetailDialogFragment;

    private DatabaseReference vendorRef;
    private ChildEventListener receivedOrderEventListener;

    private AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");

        orderDetailDialogFragment = new OrderDetailDialogFragment();

        rvReceivedOrders = (RecyclerView) findViewById(R.id.rvReceivedOrders);
        receivedOrdersAdapter = new ReceivedOrdersAdapter();

        rvReceivedOrders.setLayoutManager(new LinearLayoutManager(this));
        rvReceivedOrders.addItemDecoration(new DividerItemDecoration(this, R.drawable.shape_divider_for_received_orders));
        rvReceivedOrders.setAdapter(receivedOrdersAdapter);
        receivedOrdersAdapter.addOnItemClickListener(new ReceivedOrdersAdapter.OnItemClickListener() {
            @Override public void onItemClick(String fileName, Order order) {
                Bundle bundle = new Bundle();
                // TODO: Order 객체를 직접 전달하지 않고, FirebaseDB에서 값을 전달 받는 방식으로 변경
                bundle.putParcelable("order", order);
                bundle.putString("fileName", fileName);
                orderDetailDialogFragment.setArguments(bundle);
                orderDetailDialogFragment.show(getSupportFragmentManager(), "order_detail");
            }
        });

        appPreferenceManager = App.getApplication(getApplicationContext()).getPreferenceManager();

        vendorRef = FirebaseDatabase.getInstance()
                .getReference("orders")
                .child("vendors")
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

        vendorRef.addChildEventListener(receivedOrderEventListener);
    }

    @Override protected void onResume() {
        super.onResume();
        // TODO: Order 객체를 직접 전달하지 않고, FirebaseDB에서 값을 전달 받는 방식으로 변경
        if (getIntent().getExtras() != null) {
            final String fileName = getIntent().getExtras().getString("orderKey");

            FirebaseDatabase.getInstance()
                    .getReference("orders")
                    .child("vendors")
                    .child(appPreferenceManager.getPhoneNumber())
                    .child(fileName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override public void onDataChange(DataSnapshot dataSnapshot) {
                            Order order = dataSnapshot.getValue(Order.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("order", order);
                            bundle.putString("fileName", fileName);
                            orderDetailDialogFragment.setArguments(bundle);
                            orderDetailDialogFragment.show(getSupportFragmentManager(), "order_detail");
                        }

                        @Override public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        vendorRef.removeEventListener(receivedOrderEventListener);
    }

    @Override public void onOrderAccepted(String fileName) {
        HashMap<String, Object> map = new HashMap<>();
        // 확인 버튼을 눌렀는지 저장하기 위한 경로
        map.put("/orders/vendors/" + appPreferenceManager.getPhoneNumber() + "/" + fileName + "/accepted/", true);
        // 식당앱을 위한 경로
        map.put("/orders/restaurants/" + fileName + "/" + appPreferenceManager.getPhoneNumber() + "/accepted/", true);
        FirebaseDatabase.getInstance()
                .getReference()
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.e("error", task.getResult().toString());
                        }
                    }
                });

    }
}
