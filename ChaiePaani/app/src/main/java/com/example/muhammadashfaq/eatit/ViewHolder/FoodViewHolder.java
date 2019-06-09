package com.example.muhammadashfaq.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView foodName;
    public ImageView foodImageView,foodFavImagView,shareFoodImgVu;
    private ItemClickListner itemClickListnerFood;

    public void setItemClickListnerFood(ItemClickListner itemClickListnerFood) {
        this.itemClickListnerFood = itemClickListnerFood;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);


        foodName=itemView.findViewById(R.id.food_list_txt_vu);
        foodImageView=itemView.findViewById(R.id.food_list_image);
        foodFavImagView=itemView.findViewById(R.id.img_vu_sub_add_to_favorites);
        shareFoodImgVu=itemView.findViewById(R.id.img_vu_sub_share_to_facebook);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        itemClickListnerFood.onClick(itemView,getAdapterPosition(),false);

    }
}
