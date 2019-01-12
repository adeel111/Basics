package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adeeliftikhar.admissionserverapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdmissionScheduleFragment extends Fragment {


    public AdmissionScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admission_schedule, container, false);
        return view;
    }

}
