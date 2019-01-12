package com.example.adeeliftikhar.admission.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adeeliftikhar.admission.Adapter.TeamRecyclerAdapter;
import com.example.adeeliftikhar.admission.DataProvider.TeamDataProvider;
import com.example.adeeliftikhar.admission.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperiorTeamFragment extends Fragment {
    RecyclerView recyclerViewTeam;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<TeamDataProvider> arrayList = new ArrayList<TeamDataProvider>();

    String[] name, designation, message;
    int[] imgView = {R.drawable.riaz, R.drawable.naveed, R.drawable.sohaib, R.drawable.husnain,
            R.drawable.nabeel, R.drawable.yasir, R.drawable.suneel, R.drawable.yousaf, R.drawable.sanawar,
            R.drawable.saleem, R.drawable.saqib, R.drawable.hamid, R.drawable.tariq};

    public SuperiorTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superior_team, container, false);

        recyclerViewTeam = view.findViewById(R.id.recycler_view_team);
        name = getResources().getStringArray(R.array.name);
        designation = getResources().getStringArray(R.array.designation);
        message = getResources().getStringArray(R.array.message);

        int i = 0;
//        Following is a for-each Loop...
        for (String heading : name) {
            TeamDataProvider dataProvider = new TeamDataProvider(imgView[i], heading, designation[i], message[i]);
            arrayList.add(dataProvider);
            i++;
        }

        adapter = new TeamRecyclerAdapter(arrayList);
        recyclerViewTeam.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerViewTeam.setLayoutManager(layoutManager);
        recyclerViewTeam.setAdapter(adapter);
        return view;
    }

}
