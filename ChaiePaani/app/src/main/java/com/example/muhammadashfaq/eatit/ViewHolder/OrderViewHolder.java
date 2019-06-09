package com.example.muhammadashfaq.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtVuOrderid,txtVuOrderStatus,txtVuOrderAdress,txtVuPrice,txtVuOrderPhone;

    private ItemClickListner itemClickListner;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuOrderid=itemView.findViewById(R.id.order_id);
        txtVuOrderStatus=itemView.findViewById(R.id.order_status);
        txtVuOrderAdress=itemView.findViewById(R.id.order_adress);
        txtVuPrice=itemView.findViewById(R.id.order_price);
        txtVuOrderPhone=itemView.findViewById(R.id.order_user_phone);

       itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {
            itemClickListner.onClick(v,getAdapterPosition(),false);
    }
}
