package com.softdesign.vkmusic.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.softdesign.vkmusic.utils.ConstantManager;
import com.softdesign.vkmusic.utils.VKMusicApplication;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

/**
 * Created by Ageev Evgeny on 13.07.2016.
 */
public class PreferencesManager {
    private SharedPreferences mSharedPreferences;
    private final Context mContext;

    public PreferencesManager() {
        mContext = VKMusicApplication.getContext();
    }
    //region Vk Auth
    public void saveVKAuthToken(VKAccessToken res) {
        if (res != null) {
            res.saveTokenToSharedPreferences(mContext, ConstantManager.VK_ACCESS_TOKEN);
        }
    }

    public VKAccessToken loadVKAuthToken() {
        return VKAccessToken.tokenFromSharedPreferences(mContext, ConstantManager.VK_ACCESS_TOKEN);
    }

    public void VKLogout() {
        VKSdk.logout();                         //vk logout
        VKAccessToken.removeTokenAtKey(mContext, ConstantManager.VK_ACCESS_TOKEN);
        //SharedPreferences.Editor editor = mSharedPreferences.edit();
        //editor.clear().apply();
    }
    //endregion
}
