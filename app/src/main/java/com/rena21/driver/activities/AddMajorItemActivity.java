package com.rena21.driver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.firebase.fcm.ToastErrorHandlingListener;
import com.rena21.driver.view.actionbar.ActionBarWithButtonViewModel;
import com.rena21.driver.view.adapter.LargeCategoryAdapter;
import com.rena21.driver.view.adapter.MiddleCategoryAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddMajorItemActivity extends BaseActivity {

    private RecyclerView rvLargeCategory;
    private RecyclerView rvMiddleCategory;
    private Button btnSaveMajorItems;

    private HashMap<String,List<String>> middleCategoryMap;
    private ArrayList<String> selectedCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_major_item);

        ActionBarWithButtonViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("주요품목 추가")
                .setCloseButtonListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        finish();
                    }
                });

        selectedCategoryList = getIntent().getStringArrayListExtra("majorItems");
        if(selectedCategoryList == null) selectedCategoryList = new ArrayList<>();

        FirebaseDbManager dbManager = App.getApplication(getApplicationContext()).getDbManager();

        rvLargeCategory = (RecyclerView) findViewById(R.id.rvLargeCategory);
        rvMiddleCategory = (RecyclerView) findViewById(R.id.rvMiddleCategory);
        btnSaveMajorItems = (Button) findViewById(R.id.btnSaveMajorItems);


        dbManager.getMiddleCategoryList(new ToastErrorHandlingListener(this) {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) return;
                 middleCategoryMap = (HashMap)dataSnapshot.getValue();
            }
        });

        final LargeCategoryAdapter.ClickLargeCategoryListener listener = new LargeCategoryAdapter.ClickLargeCategoryListener() {
            @Override public void onClickLargeCategory(String largeCategoryName) {
                if(middleCategoryMap == null) return;

                List<String> middleCategoryList = middleCategoryMap.get(largeCategoryName);
                MiddleCategoryAdapter middleCategoryAdapter = new MiddleCategoryAdapter(middleCategoryList, selectedCategoryList);
                rvMiddleCategory.setLayoutManager(new LinearLayoutManager(AddMajorItemActivity.this));
                rvMiddleCategory.setAdapter(middleCategoryAdapter);
            }
        };

        dbManager.getLargeCategoryList(new ToastErrorHandlingListener(this) {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) return;

                List largeCategoryList = (List)dataSnapshot.getValue();
                LargeCategoryAdapter adapter = new LargeCategoryAdapter(largeCategoryList, listener);
                rvLargeCategory.setLayoutManager(new LinearLayoutManager(AddMajorItemActivity.this));
                rvLargeCategory.setAdapter(adapter);
            }
        });

        btnSaveMajorItems = (Button) findViewById(R.id.btnSaveMajorItems);
        btnSaveMajorItems.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putStringArrayListExtra("majorItems", selectedCategoryList);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
