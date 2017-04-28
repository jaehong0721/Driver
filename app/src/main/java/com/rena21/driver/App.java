package com.rena21.driver;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {

    public static App getApplication(Context context) {
        return (App) context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/dohyun.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
