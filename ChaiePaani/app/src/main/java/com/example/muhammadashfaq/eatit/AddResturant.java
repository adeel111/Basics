package com.example.muhammadashfaq.eatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddResturant extends AppCompatActivity {

    EditText edtTxtPhone,edtTxtName,edtTxtPassword;
    Button btnAdd;
    LinearLayout linearLayoutMap,linearLayoutData,linearLayoutProgress;
    CircleImageView imgVuProfile;
    FloatingActionButton fabUpdatePic;

    private MapFragment mMapFragment;

    String latitude,longitude,name;

    DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resturant);
        setToolbar();
        intiazlizations();

        dbRef = FirebaseDatabase.getInstance().getReference("Resturants");


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               name=edtTxtPhone.getText().toString().trim();
               latitude=edtTxtName.getText().toString().trim();
               longitude=edtTxtPassword.getText().toString().trim();

               if(name.isEmpty() && latitude.isEmpty() && latitude.isEmpty()){
                   edtTxtName.setError("Please enter resturant name");
                   edtTxtPassword.setError("Please enter resturant latitude");
                   edtTxtPhone.setError("Please enter resturant longitude");

               }else{



                   HashMap<String,String> hashMap = new HashMap<>();
                   hashMap.put("name",name);
                   hashMap.put("latitude",latitude);
                   hashMap.put("longitude",longitude);
                   hashMap.put("status", "0");

                   dbRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           linearLayoutData.setVisibility(View.GONE);
                           linearLayoutProgress.setVisibility(View.VISIBLE);
                           Toast.makeText(AddResturant.this, "Resturant request added", Toast.LENGTH_SHORT).show();
                       }
                   });

//                   Intent intent = new Intent(AddResturantModel.this,AddResturantMapActivity.class);
//                   intent.putExtra("name",name);
//                   intent.putExtra("latitude",latitude);
//                   intent.putExtra("longitude",longitude);
//                   startActivity(intent);
               }


            }
        });



    }

    private void setToolbar() {
        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        String actionBarTitleText = "Add Resturant";
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void intiazlizations() {
        edtTxtPhone=findViewById(R.id.edt_txt_phone);
        edtTxtName=findViewById(R.id.edt_txt_name);
        edtTxtPassword=findViewById(R.id.edt_txt_password);
        linearLayoutData = findViewById(R.id.linear_layout_data);
        linearLayoutProgress  = findViewById(R.id.linear_layout_progress);
        btnAdd=findViewById(R.id.btn_signup);
        imgVuProfile=findViewById(R.id.img_vu_profile);
        fabUpdatePic=findViewById(R.id.fab_img_vu_update_profile);
    }

}
