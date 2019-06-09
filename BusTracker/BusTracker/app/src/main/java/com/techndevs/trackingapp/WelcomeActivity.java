package com.techndevs.trackingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        startSplash();

    }



    private void startSplash() {
        Thread mThread= new Thread(){
            public void run(){
                super.run();
                try {
                    Thread.sleep(3000);
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                        Intent mIntent=new Intent(WelcomeActivity.this,HomeActivity.class);
                        startActivity(mIntent);
                        finish();
                    }else {
                        Intent mIntent=new Intent(WelcomeActivity.this,LoginActivity.class);
                        startActivity(mIntent);
                        finish();
                    }

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}
