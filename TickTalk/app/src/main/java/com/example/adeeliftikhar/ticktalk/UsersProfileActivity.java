package com.example.adeeliftikhar.ticktalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.ticktalk.Common.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersProfileActivity extends AppCompatActivity {
    CircleImageView imageViewUserProfilePic;
    TextView textViewUserProfileName, textViewUserProfileStatus;
    ProgressDialog progressDialog;
    Button buttonProfileSendRequest, buttonProfileCancelRequest;

    //      FriendRequest feature References...
    String currentUserID;
    String userKey;
    DatabaseReference dbRefFriendRequest;
    DatabaseReference dbRefFriends;
    DatabaseReference mUserRef;
    String currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);
        imageViewUserProfilePic = findViewById(R.id.image_view_user_profile_pic);
        textViewUserProfileName = findViewById(R.id.text_view_user_profile_name);
        textViewUserProfileStatus = findViewById(R.id.text_view_user_profile_status);
        buttonProfileSendRequest = findViewById(R.id.button_profile_send_request);
        buttonProfileCancelRequest = findViewById(R.id.button_profile_cancel_request);

        dbRefFriendRequest = FirebaseDatabase.getInstance().getReference().child("Friend_Request");
        dbRefFriends = FirebaseDatabase.getInstance().getReference().child("Friends");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        currentState = "Not Friend";
        Intent intent = getIntent();
//        Getting the Key of User (e.g ==> dGYL2znGLkZJCcrWhjvNb3gP2fq2)...
        userKey = intent.getStringExtra("userKey");
//        Whom the user which is clicked.
        userKey = getIntent().getStringExtra("userKey");
        if (!userKey.equals(null)) {
            Toast.makeText(this, userKey, Toast.LENGTH_SHORT).show();
            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);
        }
        //retrieving data of user whole profile is clicked by all users list
        if (Common.isConnected(this)) {
            mShowProfileProgDialog();
            retrieveData();
        }
    }

    private void retrieveData() {
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                textViewUserProfileName.setText(name);
                textViewUserProfileStatus.setText(status);

                progressDialog.dismiss();
                if (!image.equals("Default")) {
                    Picasso.get().load(image).resize(360, 300).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.bg1).
                            into(imageViewUserProfilePic, new Callback() {
                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError(Exception e) {
                                }
                            });
                }
                dbRefFriendRequest.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(userKey)) {
                            String req_type = dataSnapshot.child(userKey).child("request_type").getValue().toString();
                            Toast.makeText(UsersProfileActivity.this, req_type, Toast.LENGTH_SHORT).show();

                            if (req_type.equals("received")) {
                                currentState = "req_received";
                                buttonProfileSendRequest.setEnabled(true);
                                buttonProfileSendRequest.setText("Accept Friend Request");
                            } else if (req_type.equals("sent")) {
                                currentState = "req_sent";
                                buttonProfileSendRequest.setText("Cancel");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void mShowProfileProgDialog() {
        progressDialog = new ProgressDialog(UsersProfileActivity.this);
        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Wait Till it Completed...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
//    End of onCreateMethod...

    public void sendFriendRequest(View view) {
        if (currentState.equals("Not Friend")) {
            buttonProfileSendRequest.setEnabled(false);

            dbRefFriendRequest.child(currentUserID).child(userKey).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dbRefFriendRequest.child(userKey).child(currentUserID).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                currentState = "request_sent";
                                buttonProfileSendRequest.setEnabled(true);
                                buttonProfileSendRequest.setText("Cancel Request");
                                Toast.makeText(UsersProfileActivity.this, "Request Send Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        if (currentState.equals("request_sent")) {
            dbRefFriendRequest.child(currentUserID).child(userKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dbRefFriendRequest.child(userKey).child(currentUserID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                buttonProfileSendRequest.setText("Send Request");
                                buttonProfileSendRequest.setEnabled(true);
                                currentState = "Not Friend";
                                Toast.makeText(UsersProfileActivity.this, "Request Cancelled", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        if (currentState.equals("req_received")) {
            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
            dbRefFriends.child(currentUserID).child(userKey).child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    dbRefFriends.child(userKey).child(currentUserID).child("date").setValue(currentDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dbRefFriendRequest.child(currentUserID).child(userKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        dbRefFriendRequest.child(userKey).child(currentUserID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                buttonProfileSendRequest.setText("Send Request");
                                                Toast.makeText(UsersProfileActivity.this, "Now Friends", Toast.LENGTH_SHORT).show();
                                                buttonProfileSendRequest.setEnabled(true);
                                                buttonProfileCancelRequest.setText("Un Friends");
                                                buttonProfileCancelRequest.setEnabled(true);
                                                currentState = "friend";
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void deleteFriendRequest(View view) {
        if (currentState.equals("friend")) {
            dbRefFriends.child(currentUserID).child(userKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dbRefFriends.child(userKey).child(currentUserID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                buttonProfileCancelRequest.setText("Not Friends");
                                buttonProfileCancelRequest.setEnabled(true);
                                currentState = "Not Friend";
                                Toast.makeText(UsersProfileActivity.this, "Un Friend", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
