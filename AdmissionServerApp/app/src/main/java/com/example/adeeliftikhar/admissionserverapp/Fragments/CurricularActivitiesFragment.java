package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.adeeliftikhar.admissionserverapp.Adapter.ActivitiesListAdapter;
import com.example.adeeliftikhar.admissionserverapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurricularActivitiesFragment extends Fragment {
    ListView listViewCurricularActivities;
    ActivitiesListAdapter activitiesListAdapter;

    String[] activitiesName;
    String[] activitiesText;
    int[] activitiesImages = {R.drawable.entertainment,
            R.drawable.industrial_tours,
            R.drawable.tours,
            R.drawable.excellence_award,
            R.drawable.festivalandgala,
            R.drawable.study_award,
            R.drawable.quizez};

    public CurricularActivitiesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_curricular_activities, container, false);

        listViewCurricularActivities = view.findViewById(R.id.list_view_curricular_activities);
//        Always initialized class level variables here. Otherwise it will gives exception like below
//        java.lang.IllegalStateException Fragment not attached to Activity.
//        because Fragment is not attached yet to the Activity and we are initializing the class level variables
//        before it which is wrong.

//        if(getActivity()!=null){
        activitiesName = getActivity().getResources().getStringArray(R.array.activitiesName);
        activitiesText = getActivity().getResources().getStringArray(R.array.activitiesText);
//        }

        activitiesListAdapter = new ActivitiesListAdapter(getActivity(), R.layout.list_view_curricular_activities_design,
                activitiesImages,
                activitiesName,
                activitiesText);
        listViewCurricularActivities.setAdapter(activitiesListAdapter);
        return view;
    }
}