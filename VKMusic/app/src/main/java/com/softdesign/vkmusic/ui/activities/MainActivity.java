package com.softdesign.vkmusic.ui.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.softdesign.vkmusic.R;
import com.softdesign.vkmusic.data.managers.DataManager;
import com.softdesign.vkmusic.ui.fragments.MainFragment;
import com.softdesign.vkmusic.ui.fragments.SavedFragment;
import com.softdesign.vkmusic.ui.fragments.SearchFragment;
import com.softdesign.vkmusic.utils.ConstantManager;
import com.softdesign.vkmusic.utils.NetworkStatusChecker;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private DataManager mDataManager;

    private Toolbar mToolbar;
    private ImageView mSettings, mLogout;
    private LinearLayout mTabLayout;
    private TextView mSearchTab, mSavedTab;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //mToolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        mSettings = (ImageView) findViewById(R.id.settings_btn);
        mSettings.setOnClickListener(this);
        mLogout = (ImageView) findViewById(R.id.logout_btn);
        mLogout.setOnClickListener(this);


        mTabLayout = (LinearLayout) findViewById(R.id.tab_layout);
        mSearchTab = (TextView) findViewById(R.id.search_tab);
        mSavedTab = (TextView) findViewById(R.id.saved_tab);
        mSearchTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchTabSelected();
            }
        });
        mSavedTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedTabSelected();
            }
        });

        signIn();
        showFragment("main");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                showToast(getString(R.string.notify_auth_by_VK));
                mDataManager.getPreferencesManager().saveVKAuthToken(res);
                //showFragment("main");
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

    private void searchTabSelected() {
        showFragment(ConstantManager.SEARCH_FRAGMENT_TAG);
    }

    private void savedTabSelected() {
        showFragment(ConstantManager.SAVED_FRAGMENT_TAG);
    }

    @Override
    public void onBackPressed() {
        //FragmentManager fm = getFragmentManager();

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

    private void showFragment(String tag) {
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(ConstantManager.SEARCH_FRAGMENT_TAG)) {
                fragment = SearchFragment.newInstance(mQuery);
            } else if (tag.equals(ConstantManager.SAVED_FRAGMENT_TAG)) {
                fragment = new SavedFragment();
            } else {
                fragment = MainFragment.newInstance(mQuery, true, 0);
            }
        }

        if (tag.equals(ConstantManager.SEARCH_FRAGMENT_TAG)) {
            manageTabs(1);
        } else if (tag.equals(ConstantManager.SAVED_FRAGMENT_TAG)) {
            manageTabs(2);
        } else {
            manageTabs(0);
        }

        fm.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, tag)
            //.addToBackStack(tag)
            .commit();
    }

    public void manageTabs(int choice) {
        Log.d(TAG, "Manage tab - " + choice);
        switch (choice) {
            case 1:
                mSearchTab.setActivated(true);
                mSavedTab.setActivated(false);
                break;
            case 2:
                mSearchTab.setActivated(false);
                mSavedTab.setActivated(true);
                break;
            default:
                mSearchTab.setActivated(false);
                mSavedTab.setActivated(false);
        }
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
