package com.example.adeeliftikhar.ticktalk.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.ticktalk.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersViewHolder extends RecyclerView.ViewHolder {
    View view;
//    String name;
//    String status;
    public TextView allUserName, allUsersStatus;
    CircleImageView allUsersImage;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        allUserName = view.findViewById(R.id.all_users_name);
        allUsersStatus = view.findViewById(R.id.all_users_status);
        allUsersImage = view.findViewById(R.id.all_users_image);
    }

    public void setName(String name) {
//        this.name = name;
        allUserName.setText(name);
    }

    public void setStatus(String status) {
//        this.status = status;
        allUsersStatus.setText(status);
    }

    public void setThumbImage(String thumbImage) {
        Picasso.get().load(thumbImage).placeholder(R.drawable.bg1).into(allUsersImage);
    }
}
