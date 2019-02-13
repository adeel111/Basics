package com.example.aydil.nestedrecyclerview.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aydil.nestedrecyclerview.R;
import com.example.aydil.nestedrecyclerview.dataproviders.HorizontalRVDataProviders;

import java.util.ArrayList;

public class HorizontalRVAdapter extends RecyclerView.Adapter<HorizontalRVAdapter.RecyclerViewHolder> {
    private ArrayList<HorizontalRVDataProviders> arrayList1 = new ArrayList<HorizontalRVDataProviders>();
    public HorizontalRVAdapter(ArrayList<HorizontalRVDataProviders> arrayList1){
        this.arrayList1 = arrayList1;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_recycler_view_layout,parent,false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        HorizontalRVDataProviders dataProvider = arrayList1.get(position);
        holder.imageViewCntry.setImageResource(dataProvider.getImageViewCountry());
        holder.textViewCountryNam.setText(dataProvider.getTextViewCountry());
        holder.textViewNxt.setText(dataProvider.getTextViewNext());
    }

    @Override
    public int getItemCount() {
        return arrayList1.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewCntry;
        TextView textViewCountryNam;
        TextView textViewNxt;
        public RecyclerViewHolder(View view) {
            super(view);
            imageViewCntry = (ImageView) view.findViewById(R.id.image_view_country);
            textViewCountryNam = (TextView) view.findViewById(R.id.text_view_country_name);
            textViewNxt = (TextView) view.findViewById(R.id.text_view_next);
        }
    }
}
