package com.example.adeeliftikhar.ticktalk.Fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adeeliftikhar.ticktalk.AllUsersActivity;
import com.example.adeeliftikhar.ticktalk.ChatActivity;
import com.example.adeeliftikhar.ticktalk.Model.FriendsDataModel;
import com.example.adeeliftikhar.ticktalk.ProfileActivity;
import com.example.adeeliftikhar.ticktalk.R;
import com.example.adeeliftikhar.ticktalk.UsersProfileActivity;
import com.example.adeeliftikhar.ticktalk.ViewHolder.FriendsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    RecyclerView recyclerViewFriends;
    FirebaseAuth mAuth;
    DatabaseReference friendsDbRef;
    DatabaseReference userDbRef;
    String currentUserUid;
    String name, thumb_image;


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerViewFriends = view.findViewById(R.id.recycler_view_friends);
        recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        currentUserUid = mAuth.getCurrentUser().getUid();

        friendsDbRef = FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserUid);
        friendsDbRef.keepSynced(true);
        userDbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userDbRef.keepSynced(true);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FriendsDataModel, FriendsViewHolder> adapter = new
                FirebaseRecyclerAdapter<FriendsDataModel, FriendsViewHolder>(FriendsDataModel.class,
                        R.layout.dsign_for_recycler_view_friends
                        , FriendsViewHolder.class,
                        friendsDbRef) {
                    @Override
                    protected void populateViewHolder(final FriendsViewHolder viewHolder, FriendsDataModel model, int position) {
                        viewHolder.setDate(model.getDate());

                        final String listUId=getRef(position).getKey();

                        userDbRef.child(listUId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                name = dataSnapshot.child("name").getValue().toString();
                                thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                                if (dataSnapshot.hasChild("online")) {
                                    String online = dataSnapshot.child("online").getValue().toString();
                                    viewHolder.setUserOnline(online);
                                }
                                viewHolder.setName(name);
                                viewHolder.setThumb(thumb_image, getContext());
                                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CharSequence option[] = new CharSequence[]{"Open Profile", "Send Message"};
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                                        alertDialog.setTitle("Select Options");
                                        alertDialog.setItems(option, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int i) {
                                                if (i == 0) {
                                                    Intent intent = new Intent(getContext(), UsersProfileActivity.class);
                                                    intent.putExtra("userKey", listUId);
                                                    startActivity(intent);
                                                } else {
                                                    Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                    chatIntent.putExtra("UserId", listUId);
                                                    chatIntent.putExtra("user_name", name);
                                                    chatIntent.putExtra("user_image", thumb_image);
                                                    startActivity(chatIntent);
                                                }
                                            }
                                        });
                                        alertDialog.show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                };

        adapter.notifyDataSetChanged();
        recyclerViewFriends.setAdapter(adapter);
    }
}
