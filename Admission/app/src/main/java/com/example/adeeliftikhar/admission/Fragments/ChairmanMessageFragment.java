package com.example.adeeliftikhar.admission.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.R;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.ThreeBounce;
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

    LinearLayout linearLayoutSpinKitMessage, linearLayoutMessage;
    ProgressBar progressBar;
    FadingCircle fadingCircle;

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

        progressBar = view.findViewById(R.id.spin_kit_view);
        fadingCircle = new FadingCircle();
        progressBar.setIndeterminateDrawable(fadingCircle);

        linearLayoutSpinKitMessage = view.findViewById(R.id.linear_layout_spin_kit_message);
        linearLayoutMessage = view.findViewById(R.id.linear_layout_message);

        linearLayoutMessage.setVisibility(View.GONE);

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
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
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
                        linearLayoutSpinKitMessage.setVisibility(View.GONE);
                        linearLayoutMessage.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        linearLayoutSpinKitMessage.setVisibility(View.GONE);
                        linearLayoutMessage.setVisibility(View.VISIBLE);
                        Picasso.get().load(chairmanPic).placeholder(R.drawable.place_holder).into(chairmanImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
