package com.example.muhammadashfaq.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.muhammadashfaq.eatit.R;

public class RecntlyAddedResViewHolder extends RecyclerView.ViewHolder {
    public TextView txtVuOrderid,txtVuOrderStatus,txtVuOrderPhone,txtVuOrderAdress,txtVuOrderOriginalStatus;

    public RecntlyAddedResViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuOrderStatus=itemView.findViewById(R.id.order_status);
        txtVuOrderPhone=itemView.findViewById(R.id.order_phone);
        txtVuOrderAdress=itemView.findViewById(R.id.order_adress);
        txtVuOrderOriginalStatus = itemView.findViewById(R.id.txt_vu_order_status);
    }
}
