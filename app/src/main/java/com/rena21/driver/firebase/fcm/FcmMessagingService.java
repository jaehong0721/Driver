package com.rena21.driver.firebase.fcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rena21.driver.R;
import com.rena21.driver.activities.SplashActivity;

public class FcmMessagingService extends FirebaseMessagingService {

    private static final String TAG = "DriverFcm";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // {주문=주문전달완료}
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // 서버에서 전송하는 경우, notification payload를 지정 할 수 있고, 지정하는 경우 기본 notification 에 따라 행동한다.
        if (remoteMessage.getNotification() != null) {
            // 주문전달완료
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        String orderKey = remoteMessage.getData().get("orderKey");
        String vendorPhoneNumber = remoteMessage.getData().get("vendorPhoneNumber");
        String restaurantName = remoteMessage.getData().get("restaurantName");
        String itemString = remoteMessage.getData().get("itemString");

        sendNotification(orderKey, vendorPhoneNumber, restaurantName, itemString);
    }

    private void sendNotification(String orderKey, String vendorPhoneNumber, String restaurantName, String itemString) {

        String contentText = restaurantName + "에서 새로운 주문을 요청했습니다.";

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("orderKey", orderKey);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(contentText)
                .setContentText(itemString)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}