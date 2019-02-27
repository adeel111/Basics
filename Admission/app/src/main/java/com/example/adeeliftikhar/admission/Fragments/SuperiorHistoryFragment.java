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
public class SuperiorHistoryFragment extends Fragment {

    JustifyTextView superiorHistory;
    private DatabaseReference dbRef;

    SpinKitView spinKitView;
    LinearLayout linearLayoutHistory;

    public SuperiorHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superior_history, container, false);

        linearLayoutHistory = view.findViewById(R.id.linear_layout_history);
        spinKitView = view.findViewById(R.id.spin_kit_view_history);
        superiorHistory = view.findViewById(R.id.superior_history);

        linearLayoutHistory.setVisibility(View.GONE);
        spinKitView.setVisibility(View.VISIBLE);

        dbRef = FirebaseDatabase.getInstance().getReference().child("History");
        loadHistoryFromDB();
        return view;
    }

    private void loadHistoryFromDB() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String history = dataSnapshot.child("history").getValue().toString();
                spinKitView.setVisibility(View.GONE);
                linearLayoutHistory.setVisibility(View.VISIBLE);
                superiorHistory.setText(history);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
