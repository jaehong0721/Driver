package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rena21.driver.R;

import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.adapter.ReceivedOrdersAdapter;

public class MainActivity extends BaseActivity {

    private RecyclerView rvReceivedOrders;
    private ReceivedOrdersAdapter receivedOrdersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");

        rvReceivedOrders = (RecyclerView) findViewById(R.id.rvReceivedOrders);
        receivedOrdersAdapter = new ReceivedOrdersAdapter();
        rvReceivedOrders.setLayoutManager(new LinearLayoutManager(this));
        rvReceivedOrders.setAdapter(receivedOrdersAdapter);
    }
}
