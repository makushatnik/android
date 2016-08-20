package com.cdesign.sunshine.gcm;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Ageev Evgeny on 20.08.2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        Log.d(TAG, "Starting RegistrationIntentService!!!");
        Intent i = new Intent(this, RegistrationIntentService.class);
        startService(i);
    }
}
