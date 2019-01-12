package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adeeliftikhar.admissionserverapp.Adapter.FacilityRecyclerAdapter;
import com.example.adeeliftikhar.admissionserverapp.DataProvider.FacilityDataProvider;
import com.example.adeeliftikhar.admissionserverapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampusFacilityFragment extends Fragment {
    RecyclerView recyclerViewFacility;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<FacilityDataProvider> arrayList = new ArrayList<FacilityDataProvider>();

    String[] name, text;
    int[] imgView = {R.drawable.computer_labs, R.drawable.science_labs, R.drawable.transport_facility, R.drawable.superior_cafe,
            R.drawable.superior_library};

    public CampusFacilityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campus_facility, container, false);

        recyclerViewFacility = view.findViewById(R.id.recycler_view_facility);
        name = getResources().getStringArray(R.array.facilityName);
        text = getResources().getStringArray(R.array.facilityText);

        int i = 0;
//        Following is a for-each Loop...
        for (String heading : name) {
            FacilityDataProvider facilityDataProvider = new FacilityDataProvider(imgView[i], heading, text[i]);
            arrayList.add(facilityDataProvider);
            i++;
        }

        adapter = new FacilityRecyclerAdapter(arrayList);
        recyclerViewFacility.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewFacility.setLayoutManager(layoutManager);
        recyclerViewFacility.setAdapter(adapter);

        return view;
    }

}
