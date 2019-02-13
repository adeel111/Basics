package com.example.adeeliftikhar.ticktalk;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    ImageView imageViewRotate;
    TextView textViewTranslate;
    Animation rotateAnimation, translateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Following Two Lines Are Used To Remove Status Bar...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash_screen);
        imageViewRotate = findViewById(R.id.image_view_rotate);
        textViewTranslate = findViewById(R.id.text_view_translate);

//        rotateAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.rotate_anim);
//        imageViewRotate.setAnimation(rotateAnimation);
        translateAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.translate_anim);
        textViewTranslate.setAnimation(translateAnimation);
        splashScreen();
    }

    private void splashScreen() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
//                  Intent mIntent = new Intent (MainActivity.this, SecondActivity.class);
//                  startActivity(mIntent);
//                  Another way to do this...
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
