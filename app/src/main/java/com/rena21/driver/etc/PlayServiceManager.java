package com.rena21.driver.etc;

import android.app.Activity;
import android.app.Dialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.rena21.driver.view.dialogs.Dialogs;


public class PlayServiceManager {

    public interface CheckPlayServiceListener {
        void onNext();
    }

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static void checkPlayServices(Activity activity, CheckPlayServiceListener listener) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Dialog dialog = apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setCancelable(false);
                dialog.show();
            } else {
                Dialogs.createNoSupportDeviceDialog(activity).show();
            }
        }else{
            listener.onNext();
        }
    }

}
