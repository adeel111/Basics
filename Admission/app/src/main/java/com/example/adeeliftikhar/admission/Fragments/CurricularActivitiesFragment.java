package com.example.adeeliftikhar.admission.Fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.Adapter.ActivitiesListAdapter;
import com.example.adeeliftikhar.admission.Model.ActivityModel;
import com.example.adeeliftikhar.admission.Model.FacilityModel;
import com.example.adeeliftikhar.admission.R;
import com.example.adeeliftikhar.admission.ViewHolder.ActivityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurricularActivitiesFragment extends Fragment {
    ProgressDialog progressDialogLoad;

    RecyclerView recyclerViewActivity;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    public CurricularActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_curricular_activities, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        dbRef.keepSynced(true);
        storageRef = FirebaseStorage.getInstance().getReference().child("Activities");

        recyclerViewActivity = view.findViewById(R.id.recycler_view_activity);
        recyclerViewActivity.setHasFixedSize(true);
        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(getContext()));

//      Load Data from Firebase Database...
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

        FirebaseRecyclerAdapter<ActivityModel, ActivityViewHolder> adapter = new
                FirebaseRecyclerAdapter<ActivityModel, ActivityViewHolder>
                        (ActivityModel.class,
                                R.layout.recycler_view_activity_design,
                                ActivityViewHolder.class,
                                dbRef) {

                    @Override
                    protected void populateViewHolder(ActivityViewHolder viewHolder, ActivityModel model, int position) {
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
        recyclerViewActivity.setAdapter(adapter);
    }
}
