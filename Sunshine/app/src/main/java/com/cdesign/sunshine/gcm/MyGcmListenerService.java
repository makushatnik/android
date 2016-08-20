package com.cdesign.sunshine.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cdesign.sunshine.R;
import com.cdesign.sunshine.ui.MainActivity;
import com.cdesign.sunshine.utils.ConstantManager;
import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Ageev Evgeny on 20.08.2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = MyGcmListenerService.class.getSimpleName();

    public static final int NOTIFICATION_ID = 1;

    /**
     * @param from - SenderID of the sender.
     * @param data - Data bundle containing message data as key/value pairs.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        if (from == null || from.isEmpty()) return;
        Log.d(TAG, "SenderID = " + from);
        if (data.isEmpty()) return;

        String senderId = getString(R.string.gcm_defaultSenderId);
        if (senderId.length() == 0) {
            Toast.makeText(this, "SenderID string needs to be set", Toast.LENGTH_LONG).show();
        }

        if (senderId.equals(from)) {
            String weather = data.getString(ConstantManager.GCM_EXTRA_WEATHER);
            Log.d(TAG, "weater = " + weather);
            String location = data.getString(ConstantManager.GCM_EXTRA_LOCATION);
            Log.d(TAG, "location = " + location);
            String alert = String.format(getString(R.string.gcm_weather_alert), weather, location);

            sendNotification(alert);
        }
        Log.i(TAG, "Received: " + data.toString());
    }

    /**
     * @param message - The alert message to be posted.
     */
    private void sendNotification(String message) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent =
                PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.art_storm);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.art_clear)
                        .setLargeIcon(largeIcon)
                        .setContentTitle("Weather Alert!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
