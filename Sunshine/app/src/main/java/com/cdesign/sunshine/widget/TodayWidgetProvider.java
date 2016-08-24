package com.cdesign.sunshine.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.cdesign.sunshine.utils.ConstantManager;

/**
 * Created by Ageev Evgeny on 22.08.2016.
 */
public class TodayWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context ctx, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ctx.startService(new Intent(ctx, TodayWidgetIntentService.class));
    }

    @Override
    public void onAppWidgetOptionsChanged(Context ctx, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        ctx.startService(new Intent(ctx, TodayWidgetIntentService.class));
    }

    @Override
    public void onReceive(@NonNull Context ctx, @NonNull Intent intent) {
        super.onReceive(ctx, intent);
        if (ConstantManager.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            ctx.startService(new Intent(ctx, TodayWidgetIntentService.class));
        }
    }
}
