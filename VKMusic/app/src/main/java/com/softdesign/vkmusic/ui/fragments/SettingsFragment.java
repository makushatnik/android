package com.softdesign.vkmusic.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.softdesign.vkmusic.R;

/**
 * Created by Ageev Evgeny on 26.07.2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        addPreferencesFromResource(R.xml.settings);
    }
}
