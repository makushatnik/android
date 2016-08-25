package com.softdesign.vkmusic.ui.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.softdesign.vkmusic.R;

/**
 * Created by Ageev Evgeny on 27.07.2016.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

}
