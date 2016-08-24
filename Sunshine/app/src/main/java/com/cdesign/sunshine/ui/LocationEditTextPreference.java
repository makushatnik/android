package com.cdesign.sunshine.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cdesign.sunshine.R;
import com.cdesign.sunshine.utils.ConstantManager;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;

/**
 * Created by Ageev Evgeny on 19.08.2016.
 */
public class LocationEditTextPreference extends EditTextPreference {
    private int mMinLength;
    private int mMaxLength;

    public LocationEditTextPreference(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        TypedArray a = ctx.getTheme().obtainStyledAttributes(attrs, R.styleable.LocationEditTextPreference, 0, 0);
        try {
            mMinLength = a.getInteger(R.styleable.LocationEditTextPreference_minLength,
                    ConstantManager.MINIMUM_LOCATION_LENGTH);
            mMaxLength = a.getInteger(R.styleable.LocationEditTextPreference_maxLength,
                    ConstantManager.MAXIMUM_LOCATION_LENGTH);
        } finally {
            a.recycle();
        }

        checkPlayServices();
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        View currentLocation = view.findViewById(R.id.current_location);
        if (currentLocation != null) {
            currentLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getContext(), "Woo!", Toast.LENGTH_LONG).show();
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    Context ctx = getContext();
                    Activity settings = (SettingsActivity) ctx;
                    Toast.makeText(ctx, "Starting...", Toast.LENGTH_LONG).show();
                    try {
                        settings.startActivityForResult(builder.build(settings), ConstantManager.PLACE_PICKER_REQUEST);
                        Toast.makeText(ctx, "Success!", Toast.LENGTH_LONG).show();
                    } catch (GooglePlayServicesNotAvailableException
                                            | GooglePlayServicesRepairableException e) {
                        Log.e("CUSTOM VIEW", "Google error: " + e.getMessage(), e);
                    }
                }
            });
        }
        return view;
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);

        EditText et = getEditText();
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                Dialog d = getDialog();
                if (d instanceof AlertDialog) {
                    AlertDialog dialog = (AlertDialog) d;
                    Button posBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if (s.length() < mMinLength || s.length() > mMaxLength) {
                        posBtn.setEnabled(false);
                    } else {
                        posBtn.setEnabled(true);
                    }
                }
            }
        });
    }

    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getContext());
//        if (resultCode == ConnectionResult.SUCCESS) {
            setWidgetLayoutResource(R.layout.pref_current_location);
            return true;
//        }
//        Log.d("LOCATION", "Failure. resCode = " + resultCode);
//        return false;
    }
}
