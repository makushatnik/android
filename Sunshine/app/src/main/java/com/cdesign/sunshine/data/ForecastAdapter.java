package com.cdesign.sunshine.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdesign.sunshine.ui.ForecastFragment;
import com.cdesign.sunshine.R;
import com.cdesign.sunshine.utils.Utils;

/**
 * Created by Ageev Evgeny on 09.08.2016.
 */
public class ForecastAdapter extends CursorAdapter {
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private Context mContext;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    public ForecastAdapter(Context ctx, Cursor c, int flags) {
        super(ctx, c, flags);
        mContext = ctx;
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        boolean isMetric = Utils.isMetric(mContext);
        String highLowStr = Utils.formatTemperature(mContext, high) + "/" +
                Utils.formatTemperature(mContext, low);
        return highLowStr;
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {
        String highAndLow = formatHighLows(
                ForecastFragment.COL_WEATHER_MAX_TEMP,
                ForecastFragment.COL_WEATHER_MIN_TEMP
        );

        return Utils.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    @Override
    public int getItemViewType(int pos) {
        return (pos == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    /**
     * Copy/paste note: Replace existing newView() method in ForecastAdapter with this one.
     */
    @Override
    public View newView(Context ctx, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.list_item_forecast;
                break;
        }

        View view = LayoutInflater.from(ctx).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context ctx, Cursor cursor) {
        // we'll keep the UI functional with a simple (and slow!) binding.
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int viewType = getItemViewType(cursor.getPosition());
        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        int fallbackIconId;
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                fallbackIconId = Utils.getArtResourceForWeatherCondition(weatherId);
                break;
            }
            default: {
                fallbackIconId = Utils.getIconResourceForWeatherCondition(weatherId);
                break;
            }
        }

        Glide.with(mContext)
                .load(Utils.getArtUrlForWeatherCondition(mContext, weatherId))
                .error(fallbackIconId)
                .crossFade()
                .into(viewHolder.iconView);

        long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);

        viewHolder.dateView.setText(Utils.getFriendlyDayString(ctx, dateInMillis));

        //String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        String description = Utils.getStringForWeatherCondition(ctx, weatherId);
        viewHolder.descriptionView.setText(description);
        viewHolder.descriptionView.setContentDescription(ctx.getString(R.string.a11y_forecast, description));

        // Read high temperature from cursor
        String high = Utils.formatTemperature(ctx, cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP));
        viewHolder.highTempView.setText(high);
        viewHolder.highTempView.setContentDescription(ctx.getString(R.string.a11y_high_temp, high));

        // Read low temperature from cursor
        String low = Utils.formatTemperature(ctx, cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));
        viewHolder.lowTempView.setText(low);
        viewHolder.lowTempView.setContentDescription(ctx.getString(R.string.a11y_low_temp, low));
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_txt);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_txt);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_txt);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_txt);
        }
    }
}
