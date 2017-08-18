package com.rena21.driver.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.firebase.fcm.ToastErrorHandlingListener;
import com.rena21.driver.view.adapter.LargeCategoryAdapter;

import java.util.List;


public class AddMajorItemActivity extends BaseActivity {

    private RecyclerView rvLargeCategory;
    private RecyclerView rvMiddleCategory;
    private Button btnSaveMajorItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_major_item);

        final LargeCategoryAdapter.ClickLargeCategoryListener listener = new LargeCategoryAdapter.ClickLargeCategoryListener() {
            @Override public void onClickLargeCategory(String largeCategoryName) {
                Toast.makeText(AddMajorItemActivity.this, "대분류 클릭!" + largeCategoryName, Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseDbManager dbManager = App.getApplication(getApplicationContext()).getDbManager();
        dbManager.getLargeCategoryList(new ToastErrorHandlingListener(this) {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                List largeCategoryList = (List)dataSnapshot.getValue();
                LargeCategoryAdapter adapter = new LargeCategoryAdapter(largeCategoryList, listener);
                rvLargeCategory.setLayoutManager(new LinearLayoutManager(AddMajorItemActivity.this));
                rvLargeCategory.setAdapter(adapter);
            }
        });
        
        rvLargeCategory = (RecyclerView) findViewById(R.id.rvLargeCategory);
        rvMiddleCategory = (RecyclerView) findViewById(R.id.rvMiddleCategory);
        btnSaveMajorItems = (Button) findViewById(R.id.btnSaveMajorItems);
    }
}
