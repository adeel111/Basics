package com.app.adminpannel;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adminpannel.Models.DriversModel;
import com.app.adminpannel.Models.SelectDriverModel;
import com.app.adminpannel.ViewHolders.DriversViewHolder;
import com.app.adminpannel.ViewHolders.SelectDriverViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class SelectDriverActivity extends AppCompatActivity {
    private RecyclerView recyclerViewSelectDriver;
    TextView textViewName, textViewPhone, textViewVehicle, textViewAvailability;
    TextView selectDriverAvailabilityDesign;

    private DatabaseReference dbRef;
    private DatabaseReference dbRefSpecificUser;

    private FirebaseRecyclerAdapter adapter;
    private SpinKitView spinKitViewSelectDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_driver);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Select Driver");

        initialization();

//        Now load data from Firabase Database...
        loadDataFromFirebaseDB();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SelectDriverActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialization() {
        spinKitViewSelectDriver = findViewById(R.id.spin_kit_view_select_driver);
        selectDriverAvailabilityDesign = findViewById(R.id.select_driver_availability_design);

        dbRef = FirebaseDatabase.getInstance().getReference().child("DriverInfo");

        recyclerViewSelectDriver = findViewById(R.id.recycler_view_select_driver);
        recyclerViewSelectDriver.setHasFixedSize(true);
        recyclerViewSelectDriver.setLayoutManager(new LinearLayoutManager(SelectDriverActivity.this));
        dbRef.keepSynced(true);

        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = this.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(SelectDriverActivity.this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewSelectDriver.addItemDecoration(itemDecoration);
    }

    private void loadDataFromFirebaseDB() {

        FirebaseRecyclerOptions<SelectDriverModel> options = new FirebaseRecyclerOptions.Builder<SelectDriverModel>()
                .setQuery(dbRef, new SnapshotParser<SelectDriverModel>() {
                    @NonNull
                    @Override
                    public SelectDriverModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new SelectDriverModel(
                                snapshot.child("name").getValue().toString(),
                                snapshot.child("phone").getValue().toString(),
                                snapshot.child("vahicletype").getValue().toString(),
                                snapshot.child("availability").getValue().toString());
                    }
                })
                .build();

        adapter = new FirebaseRecyclerAdapter<SelectDriverModel, SelectDriverViewHolder>(options) {

            @Override
            public SelectDriverViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_select_driver_design, parent, false);

                return new SelectDriverViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SelectDriverViewHolder viewHolder, int position, @NonNull SelectDriverModel model) {
                viewHolder.setName(model.getName());
                viewHolder.setPhone(model.getPhone());
                viewHolder.setVahicleType(model.getVahicleType());
                if (model.getAvailability().equalsIgnoreCase("Available")) {
                    viewHolder.textViewSelectAvailability.setBackgroundResource(R.drawable.reactangle_green);
                    viewHolder.setAvailability(model.getAvailability());
                } else if (model.getAvailability().equalsIgnoreCase("moving")) {
                    viewHolder.textViewSelectAvailability.setBackgroundResource(R.drawable.reactangle_blue);
                    viewHolder.setAvailability(model.getAvailability());
                } else if (model.getAvailability().equalsIgnoreCase("unavailable")) {
                    viewHolder.textViewSelectAvailability.setBackgroundResource(R.drawable.reactangle_red);
                    viewHolder.setAvailability(model.getAvailability());
                }

                spinKitViewSelectDriver.setVisibility(View.GONE);
                final String userKey = getRef(position).getKey();

                dbRef = FirebaseDatabase.getInstance().getReference().child("DriverInfo").child(userKey);
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        dbRefSpecificUser = FirebaseDatabase.getInstance().getReference().child("DriverInfo").child(userKey);

                        loadSpecificUserData();
                        return false;
                    }//
                });
            }
        };
        recyclerViewSelectDriver.setAdapter(adapter);
    }

    private void loadSpecificUserData() {

        dbRefSpecificUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    This count variable will prevent unnecessary calls to onDataChange
                String name = dataSnapshot.child("name").getValue().toString();
                String number = dataSnapshot.child("phone").getValue().toString();
                String availability = dataSnapshot.child("availability").getValue().toString();

                if (availability.equals("Available")) {
                    Intent intent = new Intent(SelectDriverActivity.this, MainActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("number", number);
                    startActivity(intent);
                } else {
                    Toast.makeText(SelectDriverActivity.this, "Driver is not Available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
