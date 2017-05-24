package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.view.actionbar.ActionBarViewModel;
import com.rena21.driver.view.fragment.DeliverySummaryFragment;
import com.rena21.driver.view.layout.DeliveryOrderTabsLayout;


public class DeliveryDetailActivity extends BaseActivity implements DeliveryOrderTabsLayout.DeliveryOrderTabClickListener{

    private TextView tvRestaurantName;
    private DeliveryOrderTabsLayout tabsLayout;

    private FirebaseDbManager dbManager;

    private String fileName;
    private String vendorPhoneNumber;

    private DeliverySummaryFragment deliverySummaryFragment;

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

        deliverySummaryFragment = DeliverySummaryFragment.newInstance(fileName);

        tabsLayout = (DeliveryOrderTabsLayout) findViewById(R.id.deliveryDetailTab);
        tabsLayout.setTabClickListener(this);
        tabsLayout.showInitialView();
    }

    @Override public void onClickTab(DeliveryOrderTabsLayout.DeliveryOrderTab tab) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        switch (tab) {
            case TAB_1_ORDER_DETAILS:
                ft.replace(R.id.delivery_detail_fragment_container, deliverySummaryFragment).commit();
                break;

            case TAB_2_MODIFY:
                Toast.makeText(this, "modify", Toast.LENGTH_SHORT).show();
                break;

            case TAB_3_BILLS:
                Toast.makeText(this, "bills", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private String getPhoneNumber(String fileName) {
        return fileName.split("_")[0];
    }
}
