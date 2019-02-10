package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admissionserverapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class RulesAndRegulationsFragment extends Fragment {
    EditText rules;
    Button buttonUploadRules;

    private DatabaseReference dbRef;

    public RulesAndRegulationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rules_and_regulations, container, false);

        rules = view.findViewById(R.id.rules);
        buttonUploadRules = view.findViewById(R.id.button_upload_rules);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Rules");

        buttonUploadRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadRules();
            }
        });

//        This method will get the data from Database when activity created...
        loadDataFromDB();
        return view;
    }

    private void uploadRules() {
        String stringRules = rules.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("rules", stringRules);
        dbRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "Rules Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataFromDB() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String StringRules = dataSnapshot.child("rules").getValue().toString();
                rules.setText(StringRules);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
