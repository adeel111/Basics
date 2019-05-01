package com.example.adeeliftikhar.ambulancetracker.ViewHolders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.ambulancetracker.R;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewAmbulanceNumber, textViewDateTime;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewAmbulanceNumber = view.findViewById(R.id.text_view_ambulance_number);
        textViewDateTime = view.findViewById(R.id.text_view_date_time);
    }


    public void setTextViewAmbulanceNumber(String ambulanceName) {
        textViewAmbulanceNumber.setText(ambulanceName);
    }

    public void setDateTime(String dateTime) {
        textViewDateTime.setText(dateTime);
    }
}
