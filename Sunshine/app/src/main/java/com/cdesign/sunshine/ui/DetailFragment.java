package com.cdesign.sunshine.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdesign.sunshine.R;
import com.cdesign.sunshine.data.db.WeatherContract;
import com.cdesign.sunshine.data.db.WeatherContract.WeatherEntry;
import com.cdesign.sunshine.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ageev Evgeny on 11.08.2016.
 */
public class DetailFragment extends Fragment implements LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String DETAIL_URI = "URI";
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    public static final String DETAIL_TRANSITION_ANIMATION = "DTA";

    @BindView(R.id.detail_icon) ImageView mIconView;
    @BindView(R.id.detail_date_txt) TextView mDateView;
    @BindView(R.id.detail_forecast_txt) TextView mDescriptionView;
    @BindView(R.id.detail_high_txt) TextView mHighTempView;
    @BindView(R.id.detail_low_txt) TextView mLowTempView;
    @BindView(R.id.detail_humidity_txt) TextView mHumidityView;
    @BindView(R.id.detail_wind_txt) TextView mWindView;
    @BindView(R.id.detail_pressure_txt) TextView mPressureView;

    private String mForecast;
    private Uri mUri;
    private boolean mTransitionAnimation;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            WeatherEntry.TABLE_NAME + "." + WeatherEntry._ID,
            WeatherEntry.COLUMN_DATE,
            WeatherEntry.COLUMN_SHORT_DESC,
            WeatherEntry.COLUMN_MAX_TEMP,
            WeatherEntry.COLUMN_MIN_TEMP,
            WeatherEntry.COLUMN_HUMIDITY,
            WeatherEntry.COLUMN_PRESSURE,
            WeatherEntry.COLUMN_WIND_SPEED,
            WeatherEntry.COLUMN_DEGREES,
            WeatherEntry.COLUMN_WEATHER_ID,
            // This works because the WeatherProvider returns location data joined with
            // weather data, even though they're stored in two different tables.
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.
    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_WEATHER_HUMIDITY = 5;
    public static final int COL_WEATHER_PRESSURE = 6;
    public static final int COL_WEATHER_WIND_SPEED = 7;
    public static final int COL_WEATHER_DEGREES = 8;
    public static final int COL_WEATHER_CONDITION_ID = 9;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mUri = args.getParcelable(DETAIL_URI);
            mTransitionAnimation = args.getBoolean(DetailFragment.DETAIL_TRANSITION_ANIMATION, false);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail_start, container, false);

        ButterKnife.bind(this, rootView);
//        mIconView = (ImageView)rootView.findViewById(R.id.detail_icon);
//        mDateView = (TextView)rootView.findViewById(R.id.detail_date_txt);
//        mDescriptionView = (TextView)rootView.findViewById(R.id.detail_forecast_txt);
//        mHighTempView = (TextView)rootView.findViewById(R.id.detail_high_txt);
//        mLowTempView = (TextView)rootView.findViewById(R.id.detail_low_txt);
//        mHumidityView = (TextView)rootView.findViewById(R.id.detail_humidity_txt);
//        mWindView = (TextView)rootView.findViewById(R.id.detail_wind_txt);
//        mPressureView = (TextView)rootView.findViewById(R.id.detail_pressure_txt);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getActivity() instanceof DetailActivity) {
            inflater.inflate(R.menu.detailfragment, menu);
            finishCreatingMenu(menu);
        }
    }

    private void finishCreatingMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_share);
        menuItem.setIntent(createShareForecastIntent());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressWarnings("deprecation")
    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    public void onLocationChanged(String newLocation) {
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        ViewParent vp = getView().getParent();
        if (vp instanceof CardView) {
            ((View)vp).setVisibility(View.INVISIBLE);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (data == null || !data.moveToFirst()) { return; }

        ViewParent vp = getView().getParent();
        if (vp instanceof CardView) {
            ((View)vp).setVisibility(View.VISIBLE);
        }
        int weatherId = data.getInt(COL_WEATHER_CONDITION_ID);

        if (Utils.usingLocalGraphics(getActivity())) {
            mIconView.setImageResource(Utils.getArtResourceForWeatherCondition(weatherId));
        } else {
            Glide.with(this)
                    .load(Utils.getArtUrlForWeatherCondition(getActivity(), weatherId))
                    .error(Utils.getArtResourceForWeatherCondition(weatherId))
                    .crossFade()
                    .into(mIconView);
        }

        long date = data.getLong(COL_WEATHER_DATE);
        String dateText = Utils.getFullFriendlyDayString(getActivity(),date);
        mDateView.setText(dateText);

        //String description = data.getString(COL_WEATHER_DESC);
        String description = Utils.getStringForWeatherCondition(getActivity(), weatherId);
        mDescriptionView.setText(description);
        mDescriptionView.setContentDescription(getString(R.string.a11y_forecast, description));

        mIconView.setContentDescription(getString(R.string.a11y_forecast_icon, description));

        double high = data.getDouble(COL_WEATHER_MAX_TEMP);
        String highStr = Utils.formatTemperature(getActivity(), high);
        mHighTempView.setText(highStr);
        mHighTempView.setContentDescription(getString(R.string.a11y_high_temp, highStr));

        double low = data.getDouble(COL_WEATHER_MIN_TEMP);
        String lowStr = Utils.formatTemperature(getActivity(), low);
        mLowTempView.setText(lowStr);
        mLowTempView.setContentDescription(getString(R.string.a11y_high_temp, lowStr));

        float humidity = data.getFloat(COL_WEATHER_HUMIDITY);
        mHumidityView.setText(getString(R.string.format_humidity, humidity));
        mHumidityView.setContentDescription(getString(R.string.a11y_humidity, mHumidityView.getText()));

        float windSpeedStr = data.getFloat(COL_WEATHER_WIND_SPEED);
        float windDirStr = data.getFloat(COL_WEATHER_DEGREES);
        mWindView.setText(Utils.getFormattedWind(getActivity(), windSpeedStr, windDirStr));
        mWindView.setContentDescription(getString(R.string.a11y_wind, mWindView.getText()));

        float pressure = data.getFloat(COL_WEATHER_PRESSURE);
        mPressureView.setText(getString(R.string.format_pressure, pressure));
        mPressureView.setContentDescription(getString(R.string.a11y_pressure, mPressureView.getText()));

        // We still need this for the share intent
        mForecast = String.format("%s - %s - %s/%s", dateText, description, high, low);

//        if (mShareActionProvider != null) {
//            mShareActionProvider.setShareIntent(createShareForecastIntent());
//        }

        AppCompatActivity activity = (AppCompatActivity)getActivity();
        Toolbar toolbarView = (Toolbar) getView().findViewById(R.id.toolbar);
        if ( mTransitionAnimation ) {
            activity.supportStartPostponedEnterTransition();

            if ( null != toolbarView ) {
                activity.setSupportActionBar(toolbarView);
                activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } else {
            if ( null != toolbarView ) {
                Menu menu = toolbarView.getMenu();
                if ( null != menu) {
                    menu.clear();
                    toolbarView.inflateMenu(R.menu.detailfragment);
                    finishCreatingMenu(menu);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}
}