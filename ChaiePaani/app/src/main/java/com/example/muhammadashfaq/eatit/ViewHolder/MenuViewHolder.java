package com.example.muhammadashfaq.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView menuName;
    public ImageView imageView,imageViewfavMenu;
    private ItemClickListner itemClickListner;


    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public MenuViewHolder(@NonNull View itemView)
    {
        super(itemView);


        menuName=itemView.findViewById(R.id.menu_txt_vu);
        imageView=itemView.findViewById(R.id.menu_image);
        imageViewfavMenu=itemView.findViewById(R.id.img_vu_main_add_to_favorites);

        itemView.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(itemView,getAdapterPosition(),false);
    }

}
