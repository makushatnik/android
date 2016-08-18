package com.cdesign.sunshine.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cdesign.sunshine.sync.SunshineAuthenticator;

/**
 * Created by RealXaker on 18.08.2016.
 */
public class SunshineAuthenticatorService extends Service {
    private SunshineAuthenticator mAuth;

    @Override
    public void onCreate() {
        mAuth = new SunshineAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuth.getIBinder();
    }
}
