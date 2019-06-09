package com.techndevs.displayactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

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
                    }else{
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
