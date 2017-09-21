package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.firebase.fcm.ToastErrorHandlingListener;
import com.rena21.driver.models.RepliedEstimateItem;
import com.rena21.driver.models.Reply;
import com.rena21.driver.util.AmountCalculateUtil;
import com.rena21.driver.view.DividerItemDecoration;
import com.rena21.driver.view.actionbar.ActionBarWithButtonViewModel;
import com.rena21.driver.view.adapter.InputEstimatePriceAdapter;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;


public class InputPriceOfEstimateActivity extends BaseActivity {

    private FirebaseDbManager dbManager;

    private InputEstimatePriceAdapter adapter;
    private RecyclerView rvItemsAndPrice;

    private TextView tvRepliedItemCount;
    private TextView tvAllItemCount;
    private Button btnSavePrice;

    private String vendorName;

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

        final String estimateKey = getIntent().getStringExtra("estimateKey");
        if(estimateKey == null)
            throw new RuntimeException("estimateKey가 반드시 필요합니다");

        final CountDownLatch latch = new CountDownLatch(1);

        dbManager = App.getApplication(getApplicationContext()).getDbManager();
        dbManager.getVendorName(new ToastErrorHandlingListener(this) {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                vendorName = (String)dataSnapshot.getValue();
                latch.countDown();
            }
        });


        tvRepliedItemCount = (TextView) findViewById(R.id.tvRepliedItemCount);
        tvAllItemCount = (TextView) findViewById(R.id.tvAllItemCount);

        rvItemsAndPrice = (RecyclerView) findViewById(R.id.rvItemsAndPrice);

        btnSavePrice = (Button) findViewById(R.id.btnSavePrice);
        btnSavePrice.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(adapter == null) return;
                if(tvRepliedItemCount.getText().equals("0")) {
                    Toast.makeText(InputPriceOfEstimateActivity.this, "최소한 하나 이상 품목에 가격을 입력해야 합니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(InputPriceOfEstimateActivity.this, "견적서를 전송중입니다..", Toast.LENGTH_SHORT).show();
                finish();

                ArrayList<RepliedEstimateItem> items = adapter.getItems();
                try {
                    latch.await();
                    Reply reply = new Reply();
                    reply.repliedItems = items;
                    reply.totalPrice = AmountCalculateUtil.sumOfEachEstimateItem(items);
                    reply.vendorName = vendorName;
                    reply.timeMillis = System.currentTimeMillis();

                    dbManager.setEstimateReply(estimateKey, reply);
                } catch (InterruptedException e) {
                    FirebaseCrash.report(e);
                    Toast.makeText(InputPriceOfEstimateActivity.this, "저장에 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

        adapter = new InputEstimatePriceAdapter(items, new InputEstimatePriceAdapter.ChangeReplyCountListener() {
            @Override public void onChangeReplyCount(int count) {
                tvRepliedItemCount.setText(String.valueOf(count));
            }
        });

        rvItemsAndPrice.setLayoutManager(new LinearLayoutManager(InputPriceOfEstimateActivity.this));
        rvItemsAndPrice.addItemDecoration(new DividerItemDecoration(InputPriceOfEstimateActivity.this, R.drawable.shape_divider_for_received_orders));

        rvItemsAndPrice.setAdapter(adapter);
    }
}
