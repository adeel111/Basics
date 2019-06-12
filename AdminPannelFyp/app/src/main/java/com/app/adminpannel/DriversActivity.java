package com.app.adminpannel;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adminpannel.Internet.CheckInternetConnectivity;
import com.app.adminpannel.Models.DriversModel;
import com.app.adminpannel.ViewHolders.DriversViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriversActivity extends AppCompatActivity {

    private RecyclerView recyclerViewDrivers;
    TextView textViewName, textViewEmail, textViewPhone, textViewAddress, textViewVehicle;
    Button buttonDismiss;
    AlertDialog.Builder alertDialogDelete;

    private DatabaseReference dbRef;
    private DatabaseReference dbRefSpecificUser;

    private FirebaseRecyclerAdapter adapter;
    private SpinKitView spinKitViewDrivers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("All Drivers");

        initialization();

//        Now load data from Firabase Database...
        loadDataFromFirebaseDB();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DriversActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialization() {

        spinKitViewDrivers = findViewById(R.id.spin_kit_view_drivers);
        dbRef = FirebaseDatabase.getInstance().getReference().child("DriverInfo");

        recyclerViewDrivers = findViewById(R.id.recycler_view_drivers);
        recyclerViewDrivers.setHasFixedSize(true);
        recyclerViewDrivers.setLayoutManager(new LinearLayoutManager(DriversActivity.this));
        dbRef.keepSynced(true);

        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = this.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(DriversActivity.this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewDrivers.addItemDecoration(itemDecoration);
    }

    private void loadDataFromFirebaseDB() {

        FirebaseRecyclerOptions<DriversModel> options = new FirebaseRecyclerOptions.Builder<DriversModel>()
                .setQuery(dbRef, new SnapshotParser<DriversModel>() {
                    @NonNull
                    @Override
                    public DriversModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new DriversModel(snapshot.child("name").getValue().toString(),
                                snapshot.child("email").getValue().toString(),
                                snapshot.child("phone").getValue().toString());
                    }
                })
                .build();

        adapter = new FirebaseRecyclerAdapter<DriversModel, DriversViewHolder>(options) {
            @Override
            public DriversViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recycler_view_drivers_design, parent, false);

                return new DriversViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(DriversViewHolder viewHolder, final int position, DriversModel model) {
                viewHolder.setName(model.getName());
                viewHolder.setEmail(model.getEmail());
                viewHolder.setPhone(model.getPhone());

                spinKitViewDrivers.setVisibility(View.GONE);

                final String userKey = getRef(position).getKey();

                dbRef = FirebaseDatabase.getInstance().getReference().child("DriverInfo").child(userKey);
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        dbRefSpecificUser = FirebaseDatabase.getInstance().getReference().child("DriverInfo").child(userKey);

                        registerForContextMenu(view);
                        return false;
                    }//
                });
            }
        };
        recyclerViewDrivers.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Select an Action");
        menu.add(0, 0, 0, "1. View");//groupId, itemId, order, title
        menu.add(0, 1, 1, "2. Delete");

//        Way to use Context Menu in Fragments...
//        MenuInflater inflater = getActivity().getMenuInflater();
//        inflater.inflate(R.menu.team_menu, menu);
//        menu.setHeaderTitle("Choose one");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == 0) {
            if (!CheckInternetConnectivity.isConnectedtoInternet(DriversActivity.this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                loadSpecificUserFromFirebaseDB();
                return true;
            }
        }
        if (id == 1) {
            showAlertDialogDelete();
            return true;
        }
        return true;
    }

    private void loadSpecificUserFromFirebaseDB() {

        dbRefSpecificUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                    This count variable will prevent unnecessary calls to onDataChange
                String name = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String vehicle = dataSnapshot.child("vahicletype").getValue().toString();

                showAlertDialogBoxView(name, email, phone, address, vehicle);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void showAlertDialogBoxView(String name, String email, String phone, String address, String vehicle) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(DriversActivity.this);
        View view = LayoutInflater.from(DriversActivity.this).inflate(R.layout.driver_alert_dialog_design, null);

        final CircleImageView imageViewDriver = view.findViewById(R.id.driver_image_design);

//        if (!image.equals("false")) {
//            Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder).into(imageViewDriver, new Callback() {
//                @Override
//                public void onSuccess() {
////                    getImage();
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder).into(imageViewDriver);
//                }
//            });
//        }


        textViewName = view.findViewById(R.id.driver_name_design);
        textViewEmail = view.findViewById(R.id.driver_email_design);
        textViewPhone = view.findViewById(R.id.driver_phone_design);
        textViewAddress = view.findViewById(R.id.driver_address_design);
        textViewVehicle = view.findViewById(R.id.driver_vehicle_design);
        buttonDismiss = view.findViewById(R.id.button_dismiss);


        textViewName.setText(name);
        textViewEmail.setText(email);
        textViewPhone.setText(phone);
        textViewAddress.setText(address);
        textViewVehicle.setText(vehicle);

        builder.setView(view);
        builder.setCancelable(false);

//        This is the way to dismiss AlertDialog by Custom button instead of setPositive or
//        setNegative Button...
        final AlertDialog alertDialog = builder.show();
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showAlertDialogDelete() {
        alertDialogDelete = new AlertDialog.Builder(DriversActivity.this);
        alertDialogDelete.setTitle("Delete");
        alertDialogDelete.setMessage("Do you want to Delete this driver?");
        alertDialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Call DeleteMember method to delete member from firebase...
                if (!CheckInternetConnectivity.isConnectedtoInternet(DriversActivity.this)) {
                    Toast.makeText(DriversActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    if (!CheckInternetConnectivity.isConnectedtoInternet(DriversActivity.this)) {
                        Toast.makeText(DriversActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    } else {
                        deleteMember();
                    }
                }
            }
        });
        alertDialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogDelete.setCancelable(false);
        alertDialogDelete.show();
    }

    private void deleteMember() {
        dbRefSpecificUser.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(DriversActivity.this, "Driver is Deleted", Toast.LENGTH_SHORT).show();
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
