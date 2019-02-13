package com.example.adeeliftikhar.messaging;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText num, msg;
    Button buttonSendMessage;
    String phoneNo;
    String message;
    int i = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        num = findViewById(R.id.num);
        msg = findViewById(R.id.msg);
        buttonSendMessage = findViewById(R.id.buttonSendMessage);

    }

    public void sendSMS(View view) {
        phoneNo = num.getText().toString();
        message = msg.getText().toString();
        if (isSMSPermissionGranted()) {
            for (i = 0; i < 50; i++) {
                sendSMS(phoneNo, message);
            }
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

    public void sendSMS(String phoneNo, String msg) {
        try {
            if (phoneNo.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please Enter Data", Toast.LENGTH_SHORT).show();
            } else {

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//                Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
