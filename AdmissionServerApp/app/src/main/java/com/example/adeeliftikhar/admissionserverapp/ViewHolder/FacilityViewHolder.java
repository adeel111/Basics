package com.example.adeeliftikhar.admissionserverapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.admissionserverapp.R;
import com.squareup.picasso.Picasso;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class FacilityViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewFacilityName;
    private JustifyTextView facilityDescription;
    private ImageView imageViewFacility;

    public FacilityViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewFacilityName = view.findViewById(R.id.facility_name);
        facilityDescription = view.findViewById(R.id.facility_description);
        imageViewFacility = view.findViewById(R.id.facility_image);
    }

    public void setName(String name) {
        textViewFacilityName.setText(name);
    }

    public void setDescription(String description) {
        facilityDescription.setText(description);
    }

    public void setImage(String image) {
        Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder).into(imageViewFacility);
    }
}
