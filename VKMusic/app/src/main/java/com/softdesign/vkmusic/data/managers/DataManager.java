package com.softdesign.vkmusic.data.managers;

import android.content.Context;

import com.softdesign.vkmusic.utils.VKMusicApplication;

/**
 * Created by Ageev Evgeny on 13.07.2016.
 */
public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;
    private final PreferencesManager mPreferencesManager;

    private DataManager() {
        mPreferencesManager = new PreferencesManager();
        mContext = VKMusicApplication.getContext();
    }

    public static DataManager getInstance() {
        if (INSTANCE == null) {
            synchronized (new Object()) {
                INSTANCE = new DataManager();
            }
        }
        return INSTANCE;
    }

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getContext() {
        return mContext;
    }
}
