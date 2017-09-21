package com.rena21.driver;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.network.ConnectivityIntercepter;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {

    private Retrofit retrofit;
    private AppPreferenceManager appPreferenceManager;
    private FirebaseDbManager dbManager;

    public static App getApplication(Context context) {
        return (App) context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getString(R.string.fonts_path))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        appPreferenceManager = new AppPreferenceManager(this);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityIntercepter(this))
                .build();
        String url = getString(R.string.server_address);
        retrofit = new Retrofit
                .Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public AppPreferenceManager getPreferenceManager() {
        return appPreferenceManager;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public FirebaseDbManager getDbManager() {
        if(dbManager == null)
            dbManager = new FirebaseDbManager(FirebaseDatabase.getInstance(), appPreferenceManager.getPhoneNumber());
        return dbManager;
    }
}
