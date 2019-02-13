package com.example.adeeliftikhar.implicitintent;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText phoneNumber;
    Button buttonCall, buttonDialPad, buttonContact, buttonBrowser, buttonCallLog,
            buttonGallery, buttonCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//          To remove TitleBar...
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        phoneNumber = findViewById(R.id.phone_number);
        buttonCall = findViewById(R.id.button_call);
        buttonDialPad = findViewById(R.id.button_dial_pad);
        buttonContact = findViewById(R.id.button_contact);
        buttonBrowser = findViewById(R.id.button_browser);
        buttonCallLog = findViewById(R.id.button_call_log);
        buttonGallery = findViewById(R.id.button_gallery);
        buttonCamera = findViewById(R.id.button_camera);

//        Click Listener on all Buttons...
        final String phn = phoneNumber.getText().toString();
        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                if (phn.isEmpty()) {
                    intent.setData(Uri.parse("tel:03066798594"));
                    startActivity(intent);
               }else{
                    intent.setData(Uri.parse("tel:" + phoneNumber.getText()));
                    startActivity(intent);
                }
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
           }
       });
        buttonDialPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setAction(Intent.ACTION_DIAL);
                myIntent.setData(Uri.parse("tel:" + phoneNumber.getText()));
                startActivity(myIntent);
            }
        });
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setAction(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse("content://contacts/people/"));
                startActivity(myIntent);
            }
        });
        buttonBrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setAction(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse("https://www.google.com/"));
                startActivity(myIntent);
            }
        });
        buttonCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse("content://call_log/calls/1"));
                startActivity(myIntent);
            }
        });
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setAction(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse("content://media/external/images/media/"));
                startActivity(myIntent);
            }
        });
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent();
                myIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivity(myIntent);
            }
        });
    }
}
