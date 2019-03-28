package com.example.adeeliftikhar.admissionserverapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.admissionserverapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.biubiubiu.justifytext.library.JustifyTextView;

public class TeamViewHolder extends RecyclerView.ViewHolder {
    private View view;
    private TextView textViewName, textViewDesignation;
    private JustifyTextView textViewMessage;
    private CircleImageView imageViewTeamMember;

    public TeamViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewName = view.findViewById(R.id.team_mate_name_design);
        textViewDesignation = view.findViewById(R.id.team_mate_designation_design);
        textViewMessage = view.findViewById(R.id.team_mate_message_design);
        imageViewTeamMember = view.findViewById(R.id.team_mate_image_design);
    }

    public void setName(String name) {
        textViewName.setText(name);
    }

    public void setDesignation(String designation) {
        textViewDesignation.setText(designation);
    }

    public void setMessage(String message) {
        textViewMessage.setText(message);
    }

    public void setImage(String image) {
        Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder).into(imageViewTeamMember);
    }
}
