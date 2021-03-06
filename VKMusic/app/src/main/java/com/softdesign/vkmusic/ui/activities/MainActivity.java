package com.softdesign.vkmusic.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.softdesign.vkmusic.R;
import com.softdesign.vkmusic.data.managers.DataManager;
import com.softdesign.vkmusic.data.model.Song;
import com.softdesign.vkmusic.ui.fragments.MainFragment;
import com.softdesign.vkmusic.ui.fragments.SavedFragment;
import com.softdesign.vkmusic.ui.fragments.SearchFragment;
import com.softdesign.vkmusic.utils.AudioPlayer;
import com.softdesign.vkmusic.utils.ConstantManager;
import com.softdesign.vkmusic.utils.NetworkStatusChecker;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private DataManager mDataManager;

    private Toolbar mToolbar;
    private ImageView mSettings, mLogout;
    private TabLayout mTabLayout;
    private TabItem mSearchTab, mSavedTab;
    private int accentColorId, primaryColorId;

    private String mQuery;

    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDataManager = DataManager.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //mToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mSettings = (ImageView) findViewById(R.id.settings_btn);
        mSettings.setOnClickListener(this);
        mLogout = (ImageView) findViewById(R.id.logout_btn);
        mLogout.setOnClickListener(this);

        accentColorId = getResources().getColor(R.color.colorAccent);
        primaryColorId = getResources().getColor(R.color.colorPrimary);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mSearchTab = (TabItem) findViewById(R.id.search_tab);
        mSavedTab = (TabItem) findViewById(R.id.saved_tab);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "TAB SELECTED1!!");
                if (tab.getPosition() == 0) {
                    Log.d(TAG, "TAB SELECTED2!!!!");
                    //mSearchTab.setBackgroundColor(accentColorId);
                    Log.d(TAG, "SELECTED = " + tab.isSelected());
                    //mSavedTab.setBackgroundColor(primaryColorId);
                    showFragment(SearchFragment.newInstance(mQuery), "search");
                } else if(tab.getPosition() == 1) {
                    Log.d(TAG, "TAB SELECTED3!!!!!!");
                    showFragment(new SavedFragment(), "saved");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                showToast(getString(R.string.notify_auth_by_VK));
                mDataManager.getPreferencesManager().saveVKAuthToken(res);
                showFragment(MainFragment.newInstance(mQuery, false, 0), "main");
            }

            @Override
            public void onError(VKError error) {
                showToast(error.toString());
                Log.d(TAG, error.errorCode + ", " + error.errorReason + ", " + error.errorMessage);
                finish();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.settings_btn:
                Log.d(TAG, "Open settings!");
                openSettings();
                break;
            case R.id.logout_btn:
                Log.d(TAG, "LOGOUT!!!");
                logout();
                break;
        }
    }

    private void signIn() {
        if (NetworkStatusChecker.isNetworkAvailable(this)) {
            VKSdk.login(this, VKScope.AUDIO);
        } else {
            showSnackbar("Сеть на данный момент не доступна, попробуйте позже");
        }
    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager fm = getFragmentManager();
        //Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);

//        if (fragment == null) {
//            fragment = MainFragment.newInstance(mQuery, 0);
//            fm.beginTransaction()
//                    .add(R.id.fragmentContainer, fragment)
//                    .commit();
//        }
        fm.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            .addToBackStack(tag)
            .commit();
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void logout() {
        mDataManager.getPreferencesManager().VKLogout();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
