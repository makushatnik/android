package com.cdesign.sunshine.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cdesign.sunshine.R;
import com.cdesign.sunshine.data.ForecastAdapter;
import com.cdesign.sunshine.data.db.WeatherContract;
import com.cdesign.sunshine.gcm.RegistrationIntentService;
import com.cdesign.sunshine.sync.SunshineSyncAdapter;
import com.cdesign.sunshine.utils.ConstantManager;
import com.cdesign.sunshine.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements ForecastFragment.Callback {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean mTwoPane;
    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Uri contentUri = getIntent() != null ? getIntent().getData() : null;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mLocation = Utils.getPreferredLocation(this);

        //if (findViewById(R.id.weather_detail_container) != null) {
        if (ButterKnife.findById(this, R.id.weather_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                DetailFragment fragment = new DetailFragment();
                if (contentUri != null) {
                    Bundle args = new Bundle();
                    args.putParcelable(DetailFragment.DETAIL_URI, contentUri);
                    fragment.setArguments(args);
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.weather_detail_container, fragment,
                                ConstantManager.DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ForecastFragment forecastFragment =  ((ForecastFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_forecast));
        forecastFragment.setUseTodayLayout(!mTwoPane);
        if (contentUri != null) {
            forecastFragment.setInitialSelectedDate(WeatherContract.WeatherEntry.getDateFromUri(contentUri));
        }

        SunshineSyncAdapter.initializeSyncAdapter(this);

        if (checkPlayServices()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean sentToken = prefs.getBoolean(ConstantManager.SENT_TOKEN_TO_SERVER, false);
            if (!sentToken) {
                Log.d(LOG_TAG, "Starting RegistrationIntentService!!!");
                Intent i = new Intent(this, RegistrationIntentService.class);
                startService(i);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utils.getPreferredLocation(this);
        if (location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(
                    ConstantManager.FORECASTFRAGMENT_TAG
            );
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            DetailFragment df = (DetailFragment)getSupportFragmentManager().findFragmentByTag(
                    ConstantManager.DETAILFRAGMENT_TAG
            );
            if ( null != df ) {
                df.onLocationChanged(location);
            }
            mLocation = location;
        }
    }

    @Override
    public void onItemSelected(Uri contentUri, ForecastAdapter.ForecastViewHolder vh) {
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(DetailFragment.DETAIL_URI, contentUri);

            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.weather_detail_container, fragment,
                            ConstantManager.DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .setData(contentUri);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    new Pair<View, String>(vh.mIconView, getString(R.string.detail_icon_transition_name)));
            ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
        }
    }

    private boolean checkPlayServices() {
        boolean res = true;
        Log.d(LOG_TAG, "Ceck Started!");
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Log.d(LOG_TAG, "Get Available");
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        Log.d(LOG_TAG, "ave a result = " + resultCode);
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.d(LOG_TAG, "Need a result = " + ConnectionResult.SUCCESS);
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Log.d(LOG_TAG, "Resolvable!");
                apiAvailability.getErrorDialog(this, resultCode,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported");
                //finish();
            }
            res = false;
            //return false;
        }
        return res;
    }
}
