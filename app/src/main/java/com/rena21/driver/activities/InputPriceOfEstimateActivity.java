package com.rena21.driver.activities;

import android.os.Bundle;
import android.view.View;

import com.rena21.driver.R;
import com.rena21.driver.view.actionbar.ActionBarWithButtonViewModel;


public class InputPriceOfEstimateActivity extends BaseActivity {

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
    }
}
