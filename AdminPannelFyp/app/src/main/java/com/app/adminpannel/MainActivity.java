package com.app.adminpannel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adminpannel.Internet.CheckInternetConnectivity;
import com.app.adminpannel.Service.ListenOrder;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    TextView textViewDriverName;

    private DatabaseReference dbRefImage;
    ImageView victimImage;
    SpinKitView spinKitViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Admin Panel");

        victimImage = findViewById(R.id.victim_image);
        spinKitViewImage = findViewById(R.id.spin_kit_view_image);
        spinner = findViewById(R.id.spinner);
        textViewDriverName = findViewById(R.id.text_view_driver_name);

        getDriverNameFromIntent();

        startService(new Intent(this, ListenOrder.class));


        dbRefImage = FirebaseDatabase.getInstance().getReference().child("CurrentVictim");
        if (!CheckInternetConnectivity.isConnectedtoInternet(MainActivity.this)) {
            spinKitViewImage.setVisibility(View.GONE);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            getIncidentImageFromDB();
        }
    }

    private void getDriverNameFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            textViewDriverName.setText(name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.drivers_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.see_drivers_list) {
            if (!CheckInternetConnectivity.isConnectedtoInternet(MainActivity.this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, DriversActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void getIncidentImageFromDB() {
        dbRefImage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Getting image from Database...
                final String chairmanPic = dataSnapshot.child("victim_image").getValue().toString();
                Picasso.get().load(chairmanPic).placeholder(R.drawable.common_pic_place_holder).into(victimImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        spinKitViewImage.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(chairmanPic).placeholder(R.drawable.common_pic_place_holder).into(victimImage);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buttonSelectDriver(View view) {
        if (!CheckInternetConnectivity.isConnectedtoInternet(MainActivity.this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(MainActivity.this, SelectDriverActivity.class);
            startActivity(intent);
        }
    }

    public void buttonNotifyDriver(View view) {
        Boolean response = validateTextData();
        if (!response) {
            Toast.makeText(this, "Plz Select Driver and Recognize Severity", Toast.LENGTH_SHORT).show();
        } else {
            if (!CheckInternetConnectivity.isConnectedtoInternet(MainActivity.this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                notifyDriver();
            }
        }
    }

    private Boolean validateTextData() {
        String text = textViewDriverName.getText().toString();
        String spinnerValue = spinner.getSelectedItem().toString();

        return !text.isEmpty() && !spinnerValue.equals("Recognize Severity");
    }

    private void notifyDriver() {
        Toast.makeText(this, "Notifying Driver", Toast.LENGTH_SHORT).show();
    }
}
