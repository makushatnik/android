package com.softdesign.vkmusic.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

/**
 * Created by Ageev Evgeny on 13.07.2016.
 */
public class VKMusicApplication extends Application {
    private static VKMusicApplication sContext;
    //private static String sTokenKey = "4D573E2C4CDD16235374D1C205AF7BDD49ABB046";
    //private static String sTokenKey = "BbthCSyQLjmlknjJW5Zy";
    private static SharedPreferences sSharedPreferences;

    public VKMusicApplication() {}

    public static Context getContext() { return sContext; }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
    }

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Log.d("VKMUSIC APP", "VKAccessToken is invalid! old - " + oldToken);
            }
        }
    };
}
