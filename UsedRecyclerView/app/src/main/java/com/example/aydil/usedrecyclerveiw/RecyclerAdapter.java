package com.example.aydil.usedrecyclerveiw;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Adeel Iftikhar on 2/26/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<DataProvider> arrayList = new ArrayList<DataProvider>();

    public RecyclerAdapter(ArrayList<DataProvider> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        DataProvider dataProvider = arrayList.get(position);
        holder.imageView.setImageResource(dataProvider.getImgView());
        holder.boldHeading.setText(dataProvider.getBoldHeading());
        holder.text2.setText(dataProvider.getText2());
        holder.text3.setText(dataProvider.getText3());
        holder.likes.setText(dataProvider.getLikes());
        holder.likesAmount.setText(dataProvider.getLikesAmount());
        holder.disLikes.setText(dataProvider.getDisLikes());
        holder.disLikesAmount.setText(dataProvider.getDisLikesAmount());
        holder.neutral.setText(dataProvider.getNeutral());
        holder.neutralAmount.setText(dataProvider.getNeutralAmount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView boldHeading, text2, text3, likes, likesAmount, disLikes, disLikesAmount, neutral, neutralAmount;

        public RecyclerViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            boldHeading = (TextView) view.findViewById(R.id.bold_heading);
            text2 = (TextView) view.findViewById(R.id.text2);
            text3 = (TextView) view.findViewById(R.id.text3);
            likes = (TextView) view.findViewById(R.id.likes);
            likesAmount = (TextView) view.findViewById(R.id.like_amount);
            disLikes = (TextView) view.findViewById(R.id.dis_likes);
            disLikesAmount = (TextView) view.findViewById(R.id.dis_likes_amount);
            neutral = (TextView) view.findViewById(R.id.neutral);
            neutralAmount = (TextView) view.findViewById(R.id.neutral_amount);

        }
    }
}