package com.example.adeeliftikhar.admissionserverapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.admissionserverapp.DataProvider.TeamDataProvider;
import com.example.adeeliftikhar.admissionserverapp.R;

import java.util.ArrayList;

public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<TeamDataProvider> arrayList = new ArrayList<TeamDataProvider>();

    public TeamRecyclerAdapter(ArrayList<TeamDataProvider> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_team_design, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        TeamDataProvider dataProvider = arrayList.get(position);
        holder.image.setImageResource(dataProvider.getTeamMateImage());
        holder.name.setText(dataProvider.getTeamMateName());
        holder.designation.setText(dataProvider.getTeamMateDesignation());
        holder.message.setText(dataProvider.getTeamMateMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, designation, message;

        public RecyclerViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.team_mate_image);
            name = view.findViewById(R.id.team_mate_name);
            designation = view.findViewById(R.id.team_mate_designation);
            message = view.findViewById(R.id.team_mate_message);
        }
    }
}
