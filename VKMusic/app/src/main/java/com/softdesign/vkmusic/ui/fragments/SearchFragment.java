package com.softdesign.vkmusic.ui.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.softdesign.vkmusic.R;
import com.softdesign.vkmusic.utils.ConstantManager;

/**
 * Created by Ageev Evgeny on 26.07.2016.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = SearchFragment.class.getSimpleName();

    private EditText mQueryEdit;
    private String mQuery;
    private ImageView mSearch;
    private CheckBox myAudio;

    public static SearchFragment newInstance(String query) {
        SearchFragment fragment = new SearchFragment();

        Bundle args = new Bundle();
        args.putString(ConstantManager.SEARCH_QUERY, query);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        Bundle args = getArguments();
        mQuery = args.getString(ConstantManager.SEARCH_QUERY, "null");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
        View v = inflater.inflate(R.layout.fragment_search, parent, false);

        mQueryEdit = (EditText)v.findViewById(R.id.search_et);
        myAudio = (CheckBox)v.findViewById(R.id.my_audio_cbx);
        mSearch = (ImageView)v.findViewById(R.id.search_btn);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //searchByQuery();
                mQuery = mQueryEdit.getText().toString().trim();
                Log.d("SEARCH", "QUERY = " + mQuery);
                FragmentManager fm = getFragmentManager();

                MainFragment main = (MainFragment) fm.findFragmentByTag("main");
                if (main == null) {
                    Log.d("SEARCH", "NULL!");
                    main = MainFragment.newInstance(mQuery, myAudio.isChecked(), 0);
                    fm.beginTransaction()
                            .add(main, "main")
                            .commit();
                } else {
                    main.setSearchParams(mQuery, myAudio.isChecked());
                    fm.beginTransaction()
                            .replace(R.id.fragmentContainer, main, "main")
                            .commit();
                }
                //main.setTargetFragment(SearchFragment.this, 0);
            }
        });

        //searchByQuery();

        return v;
    }


}
