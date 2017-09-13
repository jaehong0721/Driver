package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.firebase.fcm.ToastErrorHandlingListener;
import com.rena21.driver.models.RepliedEstimateItem;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.actionbar.ActionBarWithButtonViewModel;
import com.rena21.driver.view.adapter.InputEstimatePriceAdapter;

import java.util.ArrayList;


public class InputPriceOfEstimateActivity extends BaseActivity {

    private FirebaseDbManager dbManager;

    private RecyclerView rvItemsAndPrice;
    private TextView tvRepliedItemCount;
    private TextView tvAllItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_price_of_estimate);

        ActionBarWithButtonViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("견적가 입력")
                .setCloseButtonListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        finish();
                    }
                });

        String estimateKey = getIntent().getStringExtra("estimateKey");
        if(estimateKey == null)
            throw new RuntimeException("estimateKey가 반드시 필요합니다");

        dbManager = App.getApplication(getApplicationContext()).getDbManager();

        tvRepliedItemCount = (TextView) findViewById(R.id.tvRepliedItemCount);
        tvAllItemCount = (TextView) findViewById(R.id.tvAllItemCount);

        rvItemsAndPrice = (RecyclerView) findViewById(R.id.rvItemsAndPrice);

        String what = getIntent().getStringExtra("what");

        switch (what) {
            case "input":

                dbManager.getEstimateItems(estimateKey, new ToastErrorHandlingListener(this) {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        setDataOnView(dataSnapshot);
                    }
                });

                break;

            case "modify":

                dbManager.getRepliedItems(estimateKey, new ToastErrorHandlingListener(this) {
                    @Override public void onDataChange(DataSnapshot dataSnapshot) {
                        setDataOnView(dataSnapshot);
                    }
                });

                break;

            default:
                throw new RuntimeException("인텐트 값 오류");
        }
    }

    private void setDataOnView(DataSnapshot dataSnapshot) {
        if(!dataSnapshot.exists())
            throw new RuntimeException("데이터가 존재하지 않습니다");

        GenericTypeIndicator<ArrayList<RepliedEstimateItem>> itemMapType = new GenericTypeIndicator<ArrayList<RepliedEstimateItem>>() {};
        ArrayList<RepliedEstimateItem> items = dataSnapshot.getValue(itemMapType);

        int repliedItemCount = 0;
        for(int i = 0; i<items.size(); i++) {
            if(items.get(i).price != 0)
                repliedItemCount++;
        }
        tvRepliedItemCount.setText(String.valueOf(repliedItemCount));
        tvAllItemCount.setText(String.valueOf(items.size()));

        InputEstimatePriceAdapter adapter = new InputEstimatePriceAdapter(items);
        rvItemsAndPrice.setLayoutManager(new LinearLayoutManager(InputPriceOfEstimateActivity.this));
        rvItemsAndPrice.addItemDecoration(new DividerItemDecoration(InputPriceOfEstimateActivity.this, R.drawable.shape_divider_for_received_orders));

        rvItemsAndPrice.setAdapter(adapter);
    }
}
