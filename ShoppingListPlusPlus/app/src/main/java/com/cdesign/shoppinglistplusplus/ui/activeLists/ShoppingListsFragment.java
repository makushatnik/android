package com.cdesign.shoppinglistplusplus.ui.activeLists;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdesign.shoppinglistplusplus.BuildConfig;
import com.cdesign.shoppinglistplusplus.R;
import com.cdesign.shoppinglistplusplus.model.ShoppingList;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;


/**
 * A simple {@link Fragment} subclass that shows a list of all shopping lists a user can see.
 * Use the {@link ShoppingListsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@EFragment(R.layout.fragment_shopping_lists)
public class ShoppingListsFragment extends Fragment {
    private static final String LOG_TAG = ShoppingListsFragment.class.getSimpleName();

    @ViewById(R.id.list_view_active_lists)
    ListView mListView;
    @ViewById(R.id.text_view_list_name)
    TextView mListName;
    @ViewById(R.id.text_view_created_by_user)
    TextView mOwner;

    public ShoppingListsFragment() {
        /* Required empty public constructor */
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * Right now there are not arguments...but eventually there will be.
     */
    public static ShoppingListsFragment newInstance() {
        ShoppingListsFragment fragment = new ShoppingListsFragment_();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }

    /**
     * Initialize instance variables with data from bundle
     */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Initalize UI elements
         */
        View rootView = inflater.inflate(R.layout.fragment_shopping_lists, container, false);
        //initializeScreen(rootView);

        //Firebase ref = new Firebase(BuildConfig.UNIQUE_FIREBASE_ROOT_URL).child("listName");
        Firebase ref = new Firebase(BuildConfig.FIREBASE_ROOT_URL).child("activeList");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.e(LOG_TAG, "The data changed");
//                String listName = (String) dataSnapshot.getValue();
//                mListName.setText(listName);

                ShoppingList shoppingList = dataSnapshot.getValue(ShoppingList.class);
                mListName.setText(shoppingList.getListName());
                mOwner.setText(shoppingList.getOwner());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        /**
         * Set interactive bits, such as click events and adapters
         */
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        return rootView;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }


    /**
     * Link layout elements from XML
     */
//    private void initializeScreen(View rootView) {
//        mListView = (ListView) rootView.findViewById(R.id.list_view_active_lists);
//        mListName = (TextView) rootView.findViewById(R.id.text_view_list_name);
//        mOwner = (TextView) rootView.findViewById(R.id.text_view_created_by_user);
//    }

    @ItemClick(R.id.list_view_active_lists)
    void onListItemClick() {
        Toast.makeText(getActivity(), "It works now!", Toast.LENGTH_LONG).show();
    }
}
