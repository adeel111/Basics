package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.adeeliftikhar.admissionserverapp.R;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperiorHistoryFragment extends Fragment {
    EditText superiorHistory;

    public SuperiorHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superior_history, container, false);
        superiorHistory = view.findViewById(R.id.superior_history);
        return view;
    }
}
