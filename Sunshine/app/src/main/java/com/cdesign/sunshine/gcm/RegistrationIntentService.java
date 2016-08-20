package com.cdesign.sunshine.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.cdesign.sunshine.R;
import com.cdesign.sunshine.utils.ConstantManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by Ageev Evgeny on 20.08.2016.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously,
            // ensure that they are processed sequentially.
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String senderId = getString(R.string.gcm_defaultSenderId);
                if (senderId.length() != 0) {
                    String token = instanceID.getToken(senderId,
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    sendRegistrationToServer(token);

                    prefs.edit()
                            .putBoolean(ConstantManager.SENT_TOKEN_TO_SERVER, true);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            prefs.edit()
                    .putBoolean(ConstantManager.SENT_TOKEN_TO_SERVER, false);
        }
    }

    private void sendRegistrationToServer(String token) {
        Log.i(TAG, "GCM Registration Token: " + token);
    }
}
