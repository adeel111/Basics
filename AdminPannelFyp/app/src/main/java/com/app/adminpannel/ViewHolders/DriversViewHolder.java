package com.app.adminpannel.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.adminpannel.R;

public class DriversViewHolder extends RecyclerView.ViewHolder {
    private TextView textViewName, textViewEmail, textViewPhone;

    public DriversViewHolder(@NonNull View itemView) {
        super(itemView);
        View view = itemView;
        textViewName = view.findViewById(R.id.team_mate_name_design);
        textViewEmail = view.findViewById(R.id.team_mate_email_design);
        textViewPhone = view.findViewById(R.id.team_mate_phone_design);
    }

    public void setName(String name) {
        textViewName.setText(name);
    }

    public void setEmail(String email) {
        textViewEmail.setText(email);
    }

    public void setPhone(String phone) {
        textViewPhone.setText(phone);
    }
}