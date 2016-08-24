package com.cdesign.sunshine.utils;

/**
 * Created by Ageev Evgeny on 06.08.2016.
 */
public interface ConstantManager {
    String ACTION_DATA_UPDATED = "com.cdesign.sunshine.sync.ACTION_DATA_UPDATED";

    String FORECAST_BASE_URI = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    String QUERY_PARAM = "q";
    String LAT_PARAM = "lat";
    String LON_PARAM = "lon";
    String FORMAT_PARAM = "mode";
    String UNITS_PARAM = "units";
    String DAYS_PARAM = "cnt";
    String APPID_PARAM = "APPID";
    String FORECAST_ID = "FORECAST_ID";

    int MINIMUM_LOCATION_LENGTH = 3;
    int MAXIMUM_LOCATION_LENGTH = 20;

    String SENT_TOKEN_TO_SERVER = "sentTokenToServer";

    String GCM_EXTRA_DATA = "data";
    String GCM_EXTRA_WEATHER = "weather";
    String GCM_EXTRA_LOCATION = "location";

    String FORECASTFRAGMENT_TAG = "FFTAG";
    String DETAILFRAGMENT_TAG = "DFTAG";

    int PLACE_PICKER_REQUEST = 9090;
}
