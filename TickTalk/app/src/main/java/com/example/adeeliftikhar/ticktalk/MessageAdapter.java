package com.example.adeeliftikhar.ticktalk;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.ticktalk.Model.MessagesDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<MessagesDataModel> myMassagesList;
    MessagesDataModel message;
    DatabaseReference userDbRef;
    FirebaseAuth mAuth;

    public MessageAdapter(List<MessagesDataModel> myMassagesList) {
        this.myMassagesList = myMassagesList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_message_design, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder viewHolder, int position) {

        mAuth = FirebaseAuth.getInstance();
        final String currentUser = mAuth.getCurrentUser().getUid();
        userDbRef = FirebaseDatabase.getInstance().getReference();

        message = myMassagesList.get(position);
        String fromUser = message.getFrom();
        String fromTo = message.getFrom();
        long time = message.getTimestamp();
        final String timestamp = Long.toString(time);

        if (fromUser.equals(currentUser)) {
            userDbRef.child("Users").child(currentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(timestamp);
                    String lastSeenTime = getTimeAgo.getTimeSeenAgo(lastTime, viewHolder.itemView.getContext());
                    viewHolder.msgRVTime.setText(lastSeenTime);
                    viewHolder.msgRVName.setText("You");
                    String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();


                    if (!thumbImage.equals("Default")) {
                        Picasso.get().load(thumbImage).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.bg).
                                into(viewHolder.msgRVImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            viewHolder.msgRVMessageContent.setBackgroundResource(R.drawable.going_message_design);
            viewHolder.msgRVMessageContent.setTextColor(Color.BLACK);
            viewHolder.msgRVTime.setTextColor(Color.BLACK);
        } else {
            userDbRef.child("Users").child(fromTo).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    long lastTime = Long.parseLong(timestamp);
                    String lastSeenTime = getTimeAgo.getTimeSeenAgo(lastTime, viewHolder.itemView.getContext());
                    viewHolder.msgRVTime.setText(lastSeenTime);

                    String name = dataSnapshot.child("name").getValue().toString();
                    String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();
                    viewHolder.msgRVName.setText(name);
                    if (!thumbImage.equals("Default")) {
                        Picasso.get().load(thumbImage).resize(300, 300).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.bg).
                                into(viewHolder.msgRVImage, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                    }
                                });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            viewHolder.msgRVMessageContent.setBackgroundResource(R.drawable.coming_message_design);
            viewHolder.msgRVMessageContent.setTextColor(Color.WHITE);
            viewHolder.msgRVTime.setTextColor(Color.WHITE);
        }
        viewHolder.msgRVMessageContent.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return myMassagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView msgRVImage;
        public TextView msgRVName;
        public TextView msgRVMessageContent;
        public TextView msgRVTime;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            msgRVImage = itemView.findViewById(R.id.msg_rv_image);
            msgRVName = itemView.findViewById(R.id.msg_rv_name);
            msgRVMessageContent = itemView.findViewById(R.id.msg_rv_message_content);
            msgRVTime = itemView.findViewById(R.id.msg_rv_time);
        }
    }
}
