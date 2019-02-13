package com.example.aydil.fragmentswithlistviews;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.aydil.fragmentswithlistviews.Adapters.ListViewBaseAdapter;

public class ListViewFragment extends Fragment {
    ListView listView;
    ListViewBaseAdapter listViewBaseAdapter;
    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list_view, container, false);
        listView = (ListView) rootView.findViewById(R.id.list_view);

//        If you need a Context object within your Fragment, you can call getActivity().
//        And you can also access it directly from following line of code.
//        Following line will get the context of this Fragment and store it in fragment reference.
        Context fragment = container.getContext();

        listViewBaseAdapter = new ListViewBaseAdapter(fragment);
        listView.setAdapter(listViewBaseAdapter);
        return rootView;
    }

}
