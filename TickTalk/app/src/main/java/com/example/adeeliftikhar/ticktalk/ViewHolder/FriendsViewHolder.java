package com.example.adeeliftikhar.ticktalk.ViewHolder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.adeeliftikhar.ticktalk.R;
import com.squareup.picasso.Picasso;

public class FriendsViewHolder extends RecyclerView.ViewHolder {
    View view;
    ImageView imageViewFriendsImage, imageViewFriendsActive;
    TextView textViewFriendsName, textViewFriendsDate;

    //    String friendsImage, friendsActive;
//    String friendsName, friendsDate;
    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        imageViewFriendsImage = view.findViewById(R.id.image_view_friends_image);
        imageViewFriendsActive = view.findViewById(R.id.image_view_friends_active);
        textViewFriendsName = view.findViewById(R.id.text_view_friends_name);
        textViewFriendsDate = view.findViewById(R.id.text_view_friends_date);
    }

    public void setDate(String date) {

//        this.friendsDate = date;
        textViewFriendsDate.setText(date);
    }

    public void setName(String UserName) {
//        this.friendsName = UserName;
        textViewFriendsName.setText(UserName);
    }

    public void setThumb(final String thumb, final Context context) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg1);
        Glide.with(context).load(thumb).apply(requestOptions).into(imageViewFriendsImage);
        Picasso.get().load(thumb).into(imageViewFriendsImage);
    }

    public void setUserOnline(String online) {
        if (online.equals("true")) {
            imageViewFriendsActive.setVisibility(View.VISIBLE);
        }
        else{
            imageViewFriendsActive.setVisibility(View.INVISIBLE);
//            imageViewFriendsActive.setVisibility(View.GONE);
        }
    }
}
