package com.example.adeeliftikhar.admission.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adeeliftikhar.admission.R;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RulesAndRegulationsFragment extends Fragment {
    JustifyTextView rules;

    public RulesAndRegulationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rules_and_regulations, container, false);

        rules = view.findViewById(R.id.rules);

        return view;
    }

}
