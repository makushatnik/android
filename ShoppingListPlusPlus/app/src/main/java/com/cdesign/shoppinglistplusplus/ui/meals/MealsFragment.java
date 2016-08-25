package com.cdesign.shoppinglistplusplus.ui.meals;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.cdesign.shoppinglistplusplus.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;


/**
 * A simple {@link Fragment} subclass which shows all of the meals in the Firebase database
 * Use the {@link MealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@EFragment(R.layout.fragment_meals)
public class MealsFragment extends Fragment {

    @ViewById(R.id.list_view_meals_list)
    ListView mListView;

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static MealsFragment newInstance() {
        MealsFragment fragment = new MealsFragment_();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MealsFragment() {
        /* Required empty public constructor*/
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        /* Inflate the layout for this fragment */
//        View rootView = inflater.inflate(R.layout.fragment_meals, container, false);
//
//        /**
//         * Link layout elements from XML and setup the toolbar
//         */
//        initializeScreen(rootView);
//
//        /**
//         * Set interactive bits, such as click events/adapters
//         */
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
//
//        return rootView;
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }

    @AfterViews
    void initializeScreen() {
        //mListView = (ListView) rootView.findViewById(R.id.list_view_meals_list);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer_empty, null);
        mListView.addFooterView(footer);
    }

    @ItemClick(R.id.list_view_meals_list)
    void onListItemClick() {
        Toast.makeText(getActivity(), "It works now!", Toast.LENGTH_LONG).show();
    }
}
