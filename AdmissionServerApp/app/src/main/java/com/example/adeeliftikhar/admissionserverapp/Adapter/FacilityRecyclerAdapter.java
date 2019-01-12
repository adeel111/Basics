package com.example.adeeliftikhar.admissionserverapp.Adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.admissionserverapp.DataProvider.FacilityDataProvider;
import com.example.adeeliftikhar.admissionserverapp.R;

import java.util.ArrayList;

public class FacilityRecyclerAdapter extends RecyclerView.Adapter<FacilityRecyclerAdapter.FacilityViewHolder> {
    private ArrayList<FacilityDataProvider> arrayListFacility = new ArrayList<FacilityDataProvider>();

    public FacilityRecyclerAdapter(ArrayList<FacilityDataProvider> arrayListFacility) {
        this.arrayListFacility = arrayListFacility;
    }
    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_facility_design, parent, false);
        FacilityViewHolder recyclerViewHolder = new FacilityViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder viewHolder, int position) {
        FacilityDataProvider dataProvider = arrayListFacility.get(position);
        viewHolder.imageFacility.setImageResource(dataProvider.getFacilityImage());
        viewHolder.nameFacility.setText(dataProvider.getFacilityName());
        viewHolder.textFacility.setText(dataProvider.getFacilityMessage());
    }

    @Override
    public int getItemCount() {
        return arrayListFacility.size();
    }

    public class FacilityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFacility;
        TextView nameFacility, textFacility;

        public FacilityViewHolder(View view) {
            super(view);
            imageFacility = view.findViewById(R.id.facility_image);
            nameFacility = view.findViewById(R.id.facility_name);
            textFacility = view.findViewById(R.id.facility_text);
        }
    }
}
