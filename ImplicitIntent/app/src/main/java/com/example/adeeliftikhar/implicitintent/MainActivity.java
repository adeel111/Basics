package com.example.adeeliftikhar.implicitintent;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phoneNumber.getText().toString();
                if (number.isEmpty()) {
                    String numStart = "03";
                    Uri call = Uri.parse("tel:" + numStart);
                    Intent surf = new Intent(Intent.ACTION_DIAL, call);
                    startActivity(surf);
                } else {
                    Uri call = Uri.parse("tel:" + number);
                    Intent surf = new Intent(Intent.ACTION_DIAL, call);
                    startActivity(surf);
                }
            }
        });

        buttonDialPad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "";
                Intent myIntent = new Intent();
                myIntent.setAction(Intent.ACTION_DIAL);
                myIntent.setData(Uri.parse("tel:" + number));
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
