package com.example.adeeliftikhar.admission.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.Model.FacilityModel;
import com.example.adeeliftikhar.admission.R;
import com.example.adeeliftikhar.admission.ViewHolder.FacilityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampusFacilityFragment extends Fragment {
    RecyclerView recyclerViewFacility;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ProgressDialog progressDialogLoad;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    public CampusFacilityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campus_facility, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Facilities");
        dbRef.keepSynced(true);
        storageRef = FirebaseStorage.getInstance().getReference().child("Facilities");

        recyclerViewFacility = view.findViewById(R.id.recycler_view_facility);
        recyclerViewFacility.setHasFixedSize(true);
        recyclerViewFacility.setLayoutManager(new LinearLayoutManager(getContext()));

        showProgressLoadData();
        loadDataFromFirebaseDB();

        return view;
    }

    private void showProgressLoadData() {
        progressDialogLoad = new ProgressDialog(getContext());
        progressDialogLoad.setTitle("Loading");
        progressDialogLoad.setMessage("Loading Data, Plz wait...");
        progressDialogLoad.setCancelable(false);
        progressDialogLoad.show();
    }

    private void loadDataFromFirebaseDB() {

        FirebaseRecyclerAdapter<FacilityModel, FacilityViewHolder> adapter = new
                FirebaseRecyclerAdapter<FacilityModel, FacilityViewHolder>
                        (FacilityModel.class,
                                R.layout.recycler_view_facility_design,
                                FacilityViewHolder.class,
                                dbRef) {

                    @Override
                    protected void populateViewHolder(FacilityViewHolder viewHolder, FacilityModel model, int position) {
                        viewHolder.setName(model.getName());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setImage(model.getImage());

                        progressDialogLoad.dismiss();

//                        Get Id or Key of user on Recycler Clicked Item.
//                        getRef() ==> Will Get DatabaseReference then we will get the current user key or id.

                        final String userKey = getRef(position).getKey();
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "View Clicked Key" + userKey, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
        recyclerViewFacility.setAdapter(adapter);
    }
}
