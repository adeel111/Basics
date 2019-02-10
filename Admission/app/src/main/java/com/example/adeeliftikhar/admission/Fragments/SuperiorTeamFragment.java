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

import com.example.adeeliftikhar.admission.Adapter.TeamRecyclerAdapter;
import com.example.adeeliftikhar.admission.DataProvider.TeamDataProvider;
import com.example.adeeliftikhar.admission.Model.SuperiorTeamModel;
import com.example.adeeliftikhar.admission.R;
import com.example.adeeliftikhar.admission.ViewHolder.TeamViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperiorTeamFragment extends Fragment {
    RecyclerView recyclerViewTeam;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    ProgressDialog progressDialogLoad;

    public SuperiorTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superior_team, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference().child("SuperiorTeam");
        storageRef = FirebaseStorage.getInstance().getReference().child("TeamMember");

        recyclerViewTeam = view.findViewById(R.id.recycler_view_team);
        recyclerViewTeam.setHasFixedSize(true);
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(getContext()));
        dbRef.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showProgressLoadData();
        loadDataFromFirebaseDB();
    }

    private void loadDataFromFirebaseDB() {
//        For this you have to create two other classes, One is viewHolder (to display data) and second
//        model class ( refers to name of nodes from where you are fetching data ).

        FirebaseRecyclerAdapter<SuperiorTeamModel, TeamViewHolder> adapter = new
                FirebaseRecyclerAdapter<SuperiorTeamModel, TeamViewHolder>
                        (SuperiorTeamModel.class,
                                R.layout.recycler_view_team_design,
                                TeamViewHolder.class,
                                dbRef) {

                    @Override
                    protected void populateViewHolder(TeamViewHolder viewHolder, SuperiorTeamModel model, int position) {
                        viewHolder.setName(model.getName());
                        viewHolder.setDesignation(model.getDesignation());
                        viewHolder.setMessage(model.getMessage());
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
        recyclerViewTeam.setAdapter(adapter);
    }

    private void showProgressLoadData() {
        progressDialogLoad = new ProgressDialog(getContext());
        progressDialogLoad.setTitle("Loading");
        progressDialogLoad.setMessage("Loading Data, Plz wait...");
        progressDialogLoad.setCancelable(false);
        progressDialogLoad.show();
    }
}
