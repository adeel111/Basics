package com.example.adeeliftikhar.admission.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.adeeliftikhar.admission.R;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RulesAndRegulationsFragment extends Fragment {

    JustifyTextView rules;
    DatabaseReference dbRef;

    SpinKitView spinKitViewRules;
    LinearLayout linearLayoutRules;

    public RulesAndRegulationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rules_and_regulations, container, false);

        linearLayoutRules = view.findViewById(R.id.linear_layout_rules);
        spinKitViewRules = view.findViewById(R.id.spin_kit_view_rules);

        linearLayoutRules.setVisibility(View.GONE);
        spinKitViewRules.setVisibility(View.VISIBLE);

        rules = view.findViewById(R.id.rules);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Rules");

        loadRulesFromDB();
        return view;
    }

    private void loadRulesFromDB() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String StringRules = dataSnapshot.child("rules").getValue().toString();
                spinKitViewRules.setVisibility(View.GONE);
                linearLayoutRules.setVisibility(View.VISIBLE);
                rules.setText(StringRules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
