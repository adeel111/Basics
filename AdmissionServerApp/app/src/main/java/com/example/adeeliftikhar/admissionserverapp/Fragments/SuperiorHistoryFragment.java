package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.admissionserverapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperiorHistoryFragment extends Fragment {
    EditText superiorHistory;
    String stringHistory;
    Button buttonUploadData;

    private DatabaseReference dbRef;

    public SuperiorHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superior_history, container, false);

        superiorHistory = view.findViewById(R.id.superior_history);
        buttonUploadData = view.findViewById(R.id.button_upload_data);
        dbRef = FirebaseDatabase.getInstance().getReference().child("History");

        buttonUploadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadHistory();
            }
        });

//        This method will get the data from Database when activity created...
        loadDataFromDB();
        return view;
    }

    private void uploadHistory() {
        stringHistory = superiorHistory.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("history", stringHistory);
        dbRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(), "History Uploaded Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadDataFromDB() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String history = dataSnapshot.child("history").getValue().toString();
                superiorHistory.setText(history);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
