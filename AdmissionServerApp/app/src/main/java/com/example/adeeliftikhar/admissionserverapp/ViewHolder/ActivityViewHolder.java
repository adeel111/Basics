package com.example.adeeliftikhar.admissionserverapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.admissionserverapp.R;
import com.squareup.picasso.Picasso;

import me.biubiubiu.justifytext.library.JustifyTextView;

public class ActivityViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private TextView textViewActivityName;
    private JustifyTextView activityDescription;
    private ImageView imageViewFacility;

    public ActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewActivityName = view.findViewById(R.id.activity_name);
        activityDescription = view.findViewById(R.id.activity_description);
        imageViewFacility = view.findViewById(R.id.activity_image);
    }

    public void setName(String name) {
        textViewActivityName.setText(name);
    }

    public void setDescription(String description) {
        activityDescription.setText(description);
    }

    public void setImage(String image) {
        Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder).into(imageViewFacility);
    }
}
