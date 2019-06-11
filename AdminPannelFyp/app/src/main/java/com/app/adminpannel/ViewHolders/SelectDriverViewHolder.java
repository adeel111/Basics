package com.app.adminpannel.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.adminpannel.R;

public class SelectDriverViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewSelectName;
    private TextView textViewSelectPhone;
    private TextView textViewSelectVehicle;
    public TextView textViewSelectAvailability;

    public SelectDriverViewHolder(@NonNull View itemView) {
        super(itemView);
        View view = itemView;
        textViewSelectName = view.findViewById(R.id.select_driver_name_design);
        textViewSelectPhone = view.findViewById(R.id.select_driver_phone_design);
        textViewSelectVehicle = view.findViewById(R.id.select_driver_vehicle_design);
        textViewSelectAvailability = view.findViewById(R.id.select_driver_availability_design);
    }

    public void setName(String name) {
        textViewSelectName.setText(name);
    }

    public void setPhone(String phone) {
        textViewSelectPhone.setText(phone);
    }

    public void setVahicleType(String vehicle) {
        textViewSelectVehicle.setText(vehicle);
    }

    public void setAvailability(String availability) {
        textViewSelectAvailability.setText(availability);
    }
}