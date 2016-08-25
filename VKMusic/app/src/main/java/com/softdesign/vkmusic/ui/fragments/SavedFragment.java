package com.softdesign.vkmusic.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softdesign.vkmusic.R;
import com.softdesign.vkmusic.ui.adapters.SavedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ageev Evgeny on 27.07.2016.
 */
public class SavedFragment extends Fragment {
    private static final String TAG = SavedFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private SavedAdapter mSavedAdapter;
    private List<String> mSavedSongs;
    private int mChoiceMode;

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setRetainInstance(true);

        mSavedSongs = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
        View v = inflater.inflate(R.layout.fragment_saved, parent, false);

        View emptyView = v.findViewById(R.id.empty_music_list);
        mSavedAdapter = new SavedAdapter(mSavedSongs, emptyView, mChoiceMode);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recview_saved);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSavedAdapter);

        return v;
    }
}
