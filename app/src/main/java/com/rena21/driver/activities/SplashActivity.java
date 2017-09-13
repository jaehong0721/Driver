package com.rena21.driver.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.rena21.driver.App;
import com.rena21.driver.R;
import com.rena21.driver.etc.AppPreferenceManager;
import com.rena21.driver.etc.PermissionManager;
import com.rena21.driver.etc.PlayServiceManager;
import com.rena21.driver.etc.VersionManager;
import com.rena21.driver.firebase.FirebaseDbManager;
import com.rena21.driver.firebase.fcm.ToastErrorHandlingListener;
import com.rena21.driver.network.ApiService;
import com.rena21.driver.network.NetworkUtil;
import com.rena21.driver.network.NoConnectivityException;
import com.rena21.driver.pojo.UserToken;
import com.rena21.driver.util.LauncherUtil;
import com.rena21.driver.view.dialogs.Dialogs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class SplashActivity extends BaseActivity {

    private FirebaseAuth firebaseAuth;
    private Retrofit retrofit;
    private ApiService apiService;

    private PermissionManager permissionManager;
    private AppPreferenceManager appPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        permissionManager = PermissionManager.newInstance(this);

        appPreferenceManager = App.getApplication(getApplicationContext()).getPreferenceManager();

        retrofit = App.getApplication(getApplicationContext()).getRetrofit();
        apiService = retrofit.create(ApiService.class);
        firebaseAuth = FirebaseAuth.getInstance();

        if (appPreferenceManager.getLauncherIconCreated()) {
            LauncherUtil.addLauncherIconToHomeScreen(this, getClass());
            appPreferenceManager.setLauncherIconCreated();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("test", "onResume");

        permissionManager.requestPermission(new PermissionManager.PermissionsPermittedListener() {
            @Override
            public void onAllPermissionsPermitted() {
                checkPlayService();
            }
        });
    }

    private void checkPlayService() {
        Log.d("test", "checkPlayService");

        PlayServiceManager.checkPlayServices(SplashActivity.this, new PlayServiceManager.CheckPlayServiceListener() {
            @Override
            public void onNext() {
                checkInternetConnection();
            }
        });
    }

    private void checkInternetConnection() {
        Log.d("test", "checkInternetConnection");

        if (NetworkUtil.isInternetConnected(getApplicationContext())) {
            checkAppVersion();
        } else {
            Dialogs.showNoInternetConnectivityAlertDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
    }

    private void checkAppVersion() {
        Log.d("test", "checkAppVersion");

        VersionManager.checkAppVersion(SplashActivity.this, new VersionManager.MeetRequiredVersionListener() {
            @Override
            public void onMeetRequiredVersion() {
                checkUserSignedIn();
            }
        });
    }

    private void checkUserSignedIn() {
        Log.d("test", "checkUserSignedIn");

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            storeVendorInfo();
        } else {
            requestUserToken();
        }
    }

    private void requestUserToken() {
        Log.d("test", "requestUserToken");

        appPreferenceManager.initPhoneNumber();
        apiService
                .getToken(appPreferenceManager.getPhoneNumber())
                .enqueue(new Callback<UserToken>() {
                    @Override public void onResponse(Call<UserToken> call, Response<UserToken> response) {
                        signIn(response.body().firebaseCustomAuthToken);
                    }

                    @Override public void onFailure(Call<UserToken> call, Throwable t) {
                        if (t instanceof NoConnectivityException) {
                            Toast.makeText(SplashActivity.this, "인터넷이 연결 되어 있지 않습니다. 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("test", "reqsuest token failed : " + t.getMessage());
                            FirebaseCrash.report(t);
                        }
                    }
                });
    }

    private void signIn(String customToken) {
        Log.d("test", "signIn");

        firebaseAuth
                .signInWithCustomToken(customToken)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            storeVendorInfo();
                        } else {
                            Dialogs.createPlayServiceUpdateWarningDialog(SplashActivity.this, new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    restartApp(SplashActivity.this);
                                }
                            }).show();
                        }
                    }
                });
    }

    private void storeVendorInfo() {
        Log.d("test", "sotreVendorInfo");

        final String fcmToken = appPreferenceManager.getFcmToken();

        final FirebaseDbManager dbManager = App.getApplication(getApplicationContext()).getDbManager();

        Log.d("test", "납품업체 정보 확인 시작");
        dbManager.getVendorInfoDataSnapshot(new ToastErrorHandlingListener(this) {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("test", "납품업체 정보 확인 종료");

                if(!dataSnapshot.child("vendorName").exists())
                    dbManager.setVendorName("등록필요");

                if(!dataSnapshot.child("phoneNumber").exists())
                    dbManager.setPhoneNumber();

                dbManager.setFcmId(fcmToken);

                goToMain();
            }
        });


    }

    private void goToMain() {
        Log.d("test", "goToMain");

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);

        if(getIntent().getExtras() != null) {
            String fileName = getIntent().getExtras().getString("orderKey");
            intent.putExtra("orderKey", fileName);
        }
        
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void restartApp(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
    }
}
