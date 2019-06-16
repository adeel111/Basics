package com.app.adminpannel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adminpannel.Internet.CheckInternetConnectivity;
import com.app.adminpannel.Service.ListenOrder;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.internal.Sleeper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;

    private DatabaseReference dbRefImage;
    private DatabaseReference dbRefSaveStatusAndNumber;
    private ImageView victimImage;
    private TextView buttonSelectDriver, textViewCriticalCondition, textViewNormalCondition;

    String name, number, severity, selected;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Admin Panel");

        victimImage = findViewById(R.id.victim_image);
        buttonSelectDriver = findViewById(R.id.button_select_driver);

        textViewCriticalCondition = findViewById(R.id.critical_condition);
        textViewCriticalCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewCriticalCondition.setText("Selected");
                textViewNormalCondition.setText("Normal");
            }
        });

        textViewNormalCondition = findViewById(R.id.normal_condition);
        textViewNormalCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewNormalCondition.setText("Selected");
                textViewCriticalCondition.setText("Critical");
            }
        });

        getDriverNameFromIntent();

        isSMSPermissionGranted();

        startService(new Intent(this, ListenOrder.class));

        dbRefImage = FirebaseDatabase.getInstance().getReference().child("CurrentVictim");
        if (!CheckInternetConnectivity.isConnectedtoInternet(MainActivity.this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            getIncidentImageFromDB();
        }
    }

    private void getDriverNameFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            number = intent.getStringExtra("number");
            buttonSelectDriver.setText(name);
        } else {
            buttonSelectDriver.setText("Select Driver");
        }
    }

    public boolean isSMSPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.w("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("Tag", "Permission is revoked");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Tag", "Permission is granted");
            return true;
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
        Boolean response = validateData();
        if (!response) {
            Toast.makeText(this, "Plz Select Driver and Recognize Severity", Toast.LENGTH_SHORT).show();
        } else {
            if (!CheckInternetConnectivity.isConnectedtoInternet(MainActivity.this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                sendMessageToDriver();
                saveStatusAndNumberToDB();
            }
        }
    }

    private Boolean validateData() {

        String txt = buttonSelectDriver.getText().toString();
        String condition = textViewCriticalCondition.getText().toString();
        String condition1 = textViewNormalCondition.getText().toString();

        if (condition.equals("Critical") && condition1.equals("Normal")) {
            selected = "None";
        } else {
            if (condition.equals("Selected")) {
                selected = "Critical";
            } else {
                selected = "Normal";
            }
        }
        return !txt.equals("Select Driver") && !selected.equals("None");
    }

    public void sendMessageToDriver() {
        String message = "Accident occurred, go to App to check out as soon as possible.";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    private void saveStatusAndNumberToDB() {

        severity = "Normal";

        dbRefSaveStatusAndNumber = FirebaseDatabase.getInstance().getReference().child("NotifyDriver");

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("status", "1");
        hashMap.put("number", number);
        hashMap.put("severity", severity);

        dbRefSaveStatusAndNumber.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Saved Data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
