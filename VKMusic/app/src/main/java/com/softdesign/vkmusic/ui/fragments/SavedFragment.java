package com.softdesign.vkmusic.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.softdesign.vkmusic.R;

/**
 * Created by Ageev Evgeny on 27.07.2016.
 */
public class SavedFragment extends Fragment {

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
        View v = inflater.inflate(R.layout.fragment_saved, parent, false);


        return v;
    }
}
