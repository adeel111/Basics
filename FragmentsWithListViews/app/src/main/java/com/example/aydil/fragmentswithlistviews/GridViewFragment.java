package com.example.aydil.fragmentswithlistviews;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.aydil.fragmentswithlistviews.Adapters.GridViewBaseAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class GridViewFragment extends Fragment {
    GridView gridView;
    GridViewBaseAdapter gridViewBaseAdapter;

    public GridViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_grid_view, container, false);
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        Context fragment = container.getContext();
        gridViewBaseAdapter = new GridViewBaseAdapter(fragment);
        gridView.setAdapter(gridViewBaseAdapter);
        return rootView;
    }

}
