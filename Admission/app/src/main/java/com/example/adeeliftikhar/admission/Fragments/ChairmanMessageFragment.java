package com.example.adeeliftikhar.admission.Fragments;


import android.app.ProgressDialog;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import me.biubiubiu.justifytext.library.JustifyTextView;

/**
 * A simple {@link Fragment} subclass.
 */

public class ChairmanMessageFragment extends Fragment {
    ImageView chairmanImage;
    TextView chairmanName;
    JustifyTextView chairmanMessage;
    ProgressDialog progressDialog;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

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

        dbRef = FirebaseDatabase.getInstance().getReference().child("ChairmanMessage");
        storageRef = FirebaseStorage.getInstance().getReference().child("Chairman");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromFirebse();
        showProgressDialog();
    }

    private void getDataFromFirebse() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String message = dataSnapshot.child("message").getValue().toString();
                chairmanName.setText(name);
                chairmanMessage.setText(message);
//                Getting image from Database...
                final String chairmanPic = dataSnapshot.child("chairman_image").getValue().toString();
                Picasso.get().load(chairmanPic).placeholder(R.drawable.place_holder).into(chairmanImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Exception e) {
                        progressDialog.dismiss();
                        Picasso.get().load(chairmanPic).placeholder(R.drawable.place_holder).into(chairmanImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Data, Plz wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
}
