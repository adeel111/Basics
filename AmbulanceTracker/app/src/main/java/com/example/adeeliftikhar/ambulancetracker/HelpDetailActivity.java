package com.example.adeeliftikhar.ambulancetracker;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class HelpDetailActivity extends AppCompatActivity {
    TextView textViewHelpName, textViewHelpNumber, textViewAmbulanceName, textViewDateTime;
    ImageView imageViewIncident;

    String comingItemKey;
    FirebaseAuth mAuth;
    String currentUser;
    private DatabaseReference dbRef;

    ProgressBar progressBarHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Detail of Help");

        getComingData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(HelpDetailActivity.this, HistoryActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializer() {
        textViewHelpName = findViewById(R.id.text_view_help_name);
        textViewHelpNumber = findViewById(R.id.text_view_help_number);
        textViewAmbulanceName = findViewById(R.id.text_view_help_ambulance_name);
        textViewDateTime = findViewById(R.id.text_view_help_date_time);
        imageViewIncident = findViewById(R.id.image_view_incident);
        progressBarHelp = findViewById(R.id.progress_bar_help);
        progressBarHelp.setVisibility(View.VISIBLE);
//        Initialize DB credentials...
        mAuth = FirebaseAuth.getInstance();
        currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("HelperHistory").child(currentUser).child(comingItemKey);

        if (!comingItemKey.isEmpty()) {
            getDataFromDB();
        }
    }

    private void getComingData() {
        Intent intent = getIntent();
        comingItemKey = intent.getStringExtra("itemKey");

        initializer();
    }

    private void getDataFromDB() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String number = dataSnapshot.child("number").getValue().toString();
                String ambulance = dataSnapshot.child("ambulance").getValue().toString();
                String date_time = dataSnapshot.child("date_time").getValue().toString();

                textViewHelpName.setText(name);
                textViewHelpNumber.setText(number);
                textViewAmbulanceName.setText(ambulance);
                textViewDateTime.setText(date_time);

                final String imageURI = dataSnapshot.child("imageURI").getValue().toString();
                Picasso.get().load(imageURI).placeholder(R.drawable.common_pic_place_holder).into(imageViewIncident, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBarHelp.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(imageURI).placeholder(R.drawable.common_pic_place_holder).into(imageViewIncident);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HelpDetailActivity.this, "Error ==> " + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
