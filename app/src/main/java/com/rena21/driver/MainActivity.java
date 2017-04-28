package com.rena21.driver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import view.ActionBarViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBarViewModel.createWithActionBar(getSupportActionBar())
                .setTitle("");
    }
}
