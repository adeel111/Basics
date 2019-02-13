package com.example.aydil.fragmentswithlistviews;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.aydil.fragmentswithlistviews.Adapters.ExpandableListViewAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpandableLVFragment extends Fragment {
    ExpandableListView expandableListView;
    ExpandableListViewAdapter expandableListViewAdapter;
    String[] parentViewArray = {"Computer", "Cars", "Games", "Food", "Countries"};
    String[][] childViewArray = {{"HP", "Dell", "Apple"}, {"BMW", "Honda Civic", "Mercedes"},
            {"Cricket", "Boxing", "Hockey", "Football"}, {"Mango", "Banana", "Orange", "Pizza"},
            {"Pakistan", "China", "Dubai", "England"}};
    public ExpandableLVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_expandable_lv, container, false);
        expandableListView = rootView.findViewById(R.id.expandable_list_view);
        Context fragment = container.getContext();
        expandableListViewAdapter = new ExpandableListViewAdapter(fragment,parentViewArray,childViewArray);
        expandableListView.setAdapter(expandableListViewAdapter);
        return rootView;
    }
}
