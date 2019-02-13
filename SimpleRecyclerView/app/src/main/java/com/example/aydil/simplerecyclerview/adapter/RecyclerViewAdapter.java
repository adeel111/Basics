package com.example.aydil.simplerecyclerview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aydil.simplerecyclerview.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {
    String[] countryNames;
    String[] countryCapitals;
    Context context;
//    LayoutInflater layoutInflater;
    public RecyclerViewAdapter(Context context, String[] countryNames, String[] countryCapitals) {
        this.countryNames = countryNames;
        this.countryCapitals = countryCapitals;
//        this.context = context;
//        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.inflate(R.layout.recycler_view_layout_activity, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout_activity, parent, false);

        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.countryNam.setText(countryNames[position]);
        holder.countryCap.setText(countryCapitals[position]);
    }

    @Override
    public int getItemCount() {
        return countryNames.length;
    }
//    This class will extend the Views...
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView countryNam, countryCap;

        public RecyclerViewHolder(View view) {
            super(view);
            countryNam = view.findViewById(R.id.country_name);
            countryCap = view.findViewById(R.id.capital_name);
        }
    }
}
