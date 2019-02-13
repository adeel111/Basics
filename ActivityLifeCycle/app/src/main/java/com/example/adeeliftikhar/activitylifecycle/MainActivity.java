package com.example.adeeliftikhar.activitylifecycle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "ON START", Toast.LENGTH_SHORT).show();

//        The Log.e() method is used to log errors.
//        The Log.w() method is used to log warnings.
//        The Log.i() method is used to log informational messages.
//        The Log.d() method is used to log debug messages.
//        The Log.v() method is used to log verbose messages.
//        The Log.wtf() method is used to log terrible failures
//        that should never happen. ("WTF" stands for "What a Terrible Failure!" of course.)
        Log.w( "On Start","ON START is Starting...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "ON RESUME", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "ON PAUSE", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "ON RESTART", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "ON STOP", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "ON DESTROY", Toast.LENGTH_SHORT).show();
    }
}
