package com.rena21.driver.activities;

import android.os.Bundle;

import com.rena21.driver.R;

import view.ActionBarViewModel;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");
    }
}
