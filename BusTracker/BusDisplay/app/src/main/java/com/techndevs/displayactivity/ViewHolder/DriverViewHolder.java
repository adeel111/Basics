package com.techndevs.displayactivity.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.techndevs.displayactivity.R;

public class DriverViewHolder extends RecyclerView.ViewHolder {
    public TextView txtVuOrderid,txtVuOrderName,txtVuOrderPhone,txtVuOrderBusNO,txtVuOrderEmail;


    public DriverViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuOrderName=itemView.findViewById(R.id.order_status);
        txtVuOrderPhone=itemView.findViewById(R.id.order_phone);
        txtVuOrderBusNO=itemView.findViewById(R.id.order_adress);
        txtVuOrderEmail=itemView.findViewById(R.id.order_price);

    }

}
