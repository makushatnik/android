package com.cdesign.sunshine.data;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdesign.sunshine.R;
import com.cdesign.sunshine.data.db.WeatherContract;
import com.cdesign.sunshine.ui.ForecastFragment;
import com.cdesign.sunshine.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ageev Evgeny on 09.08.2016.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    private Cursor mCursor;
    private Context mContext;
    private final ForecastAdapterOnClickHandler mClickHandler;
    private final View mEmptyView;
    private final ItemChoiceManager mICM;

    // Flag to determine if we want to use a separate view for "today".
    private boolean mUseTodayLayout = true;

    public ForecastAdapter(Context ctx, ForecastAdapterOnClickHandler dh, View emptyView, int choiceMode) {
        mContext = ctx;
        mClickHandler = dh;
        mEmptyView = emptyView;
        mICM = new ItemChoiceManager(this);
        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if ( viewGroup instanceof RecyclerView ) {
            int layoutId = -1;
            switch (viewType) {
                case VIEW_TYPE_TODAY:
                    layoutId = R.layout.list_item_forecast_today;
                    break;
                case VIEW_TYPE_FUTURE_DAY:
                    layoutId = R.layout.list_item_forecast;
                    break;
            }
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
            view.setFocusable(true);
            return new ForecastViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder viewHolder, int position) {
        mCursor.moveToPosition(position);
        int weatherId = mCursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);
        int defaultImage;
        boolean useLongToday;
        switch (getItemViewType(position)) {
            case VIEW_TYPE_TODAY:
                defaultImage = Utils.getArtResourceForWeatherCondition(weatherId);
                useLongToday = true;
                break;
            default:
                defaultImage = Utils.getIconResourceForWeatherCondition(weatherId);
                useLongToday = false;
        }

        if (Utils.usingLocalGraphics(mContext)) {
            viewHolder.mIconView.setImageResource(defaultImage);
        } else {
            Glide.with(mContext)
                    .load(Utils.getArtUrlForWeatherCondition(mContext, weatherId))
                    .error(defaultImage)
                    .crossFade()
                    .into(viewHolder.mIconView);
        }

        ViewCompat.setTransitionName(viewHolder.mIconView, "iconView" + position);
        long dateInMillis = mCursor.getLong(ForecastFragment.COL_WEATHER_DATE);

        viewHolder.mDateView.setText(Utils.getFriendlyDayString(mContext, dateInMillis, useLongToday));

        //String description = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        String description = Utils.getStringForWeatherCondition(mContext, weatherId);
        viewHolder.mDescriptionView.setText(description);
        viewHolder.mDescriptionView.setContentDescription(mContext.getString(R.string.a11y_forecast, description));

        // Read high temperature from cursor
        double high = mCursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        String highString = Utils.formatTemperature(mContext, high);
        viewHolder.mHighTempView.setText(highString);
        viewHolder.mHighTempView.setContentDescription(mContext.getString(R.string.a11y_high_temp, highString));

        // Read low temperature from cursor
        double low = mCursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        String lowString = Utils.formatTemperature(mContext, low);
        viewHolder.mLowTempView.setText(lowString);
        viewHolder.mLowTempView.setContentDescription(mContext.getString(R.string.a11y_low_temp, lowString));

        mICM.onBindViewHolder(viewHolder, position);
    }

    public void onRestoreInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public void onSaveInstanceState(Bundle outState) {
        mICM.onSaveInstanceState(outState);
    }

    public int getSelectedItemPosition() {
        return mICM.getSelectedItemPosition();
    }

    public void selectView(RecyclerView.ViewHolder viewHolder) {
        if ( viewHolder instanceof ForecastViewHolder ) {
            ForecastViewHolder vfh = (ForecastViewHolder)viewHolder;
            vfh.onClick(vfh.itemView);
        }
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
//    private String formatHighLows(double high, double low) {
//        boolean isMetric = Utils.isMetric(mContext);
//        String highLowStr = Utils.formatTemperature(mContext, high) + "/" +
//                Utils.formatTemperature(mContext, low);
//        return highLowStr;
//    }

//    private String convertCursorRowToUXFormat(Cursor cursor) {
//        String highAndLow = formatHighLows(
//                ForecastFragment.COL_WEATHER_MAX_TEMP,
//                ForecastFragment.COL_WEATHER_MIN_TEMP
//        );
//
//        return Utils.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
//                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
//                " - " + highAndLow;
//    }

    @Override
    public int getItemViewType(int pos) {
        return (pos == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getItemCount() {
        if ( null == mCursor ) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
    }


    /**
     * Cache of the children views for a forecast list item.
     */
    public class ForecastViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.list_item_icon) public ImageView mIconView;
        @BindView(R.id.list_item_date_txt) TextView mDateView;
        @BindView(R.id.list_item_forecast_txt) TextView mDescriptionView;
        @BindView(R.id.list_item_high_txt) TextView mHighTempView;
        @BindView(R.id.list_item_low_txt) TextView mLowTempView;

        public ForecastViewHolder(View view) {
            super(view);

//            mIconView = (ImageView) view.findViewById(R.id.list_item_icon);
//            mDateView = (TextView) view.findViewById(R.id.list_item_date_txt);
//            mDescriptionView = (TextView) view.findViewById(R.id.list_item_forecast_txt);
//            mHighTempView = (TextView) view.findViewById(R.id.list_item_high_txt);
//            mLowTempView = (TextView) view.findViewById(R.id.list_item_low_txt);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int dateColumnIndex = mCursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
            mClickHandler.onClick(mCursor.getLong(dateColumnIndex), this);
            mICM.onClick(this);
        }
    }

    public interface ForecastAdapterOnClickHandler {
        void onClick(Long date, ForecastViewHolder vh);
    }
}
