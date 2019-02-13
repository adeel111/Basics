package com.example.adeeliftikhar.ticktalk;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class TickTalk extends Application {
    private DatabaseReference userDatabaseRef;
    private FirebaseAuth mAuth;
    String currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser);
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("online")) {
                    if (dataSnapshot != null) {
                        userDatabaseRef.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);
//                    userDatabaseRef.child("online").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
