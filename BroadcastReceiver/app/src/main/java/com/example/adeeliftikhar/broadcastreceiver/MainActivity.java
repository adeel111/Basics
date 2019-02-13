package com.example.adeeliftikhar.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broadcastReceiver = new MyBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Registering an BroadcastReceiver...
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Un_Registering an BroadcastReceiver...
//        unregisterReceiver(broadcastReceiver);
    }
}
