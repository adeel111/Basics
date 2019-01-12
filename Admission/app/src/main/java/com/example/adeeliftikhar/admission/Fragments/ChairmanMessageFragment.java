package com.example.adeeliftikhar.admission.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.R;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChairmanMessageFragment extends Fragment {
    ImageView chairmanImage;
    TextView chairmanName;
    JustifyTextView chairmanMessage;

    public ChairmanMessageFragment() {
//       Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chairman_message, container, false);

        chairmanImage = view.findViewById(R.id.chairman_image);
        chairmanName = view.findViewById(R.id.chairman_name);
        chairmanMessage = view.findViewById(R.id.chairman_message);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromFirebse();
    }

    private void getDataFromFirebse() {
        chairmanName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Name Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
