package com.example.adeeliftikhar.admission;

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
    ImageView imageViewSplashLogo;
    TextView textViewSplash;
    Animation rotateAnimation, translateAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        removeStatusBar();
        setContentView(R.layout.activity_splash_screen);
        initializer();
        splashScreen();
    }

    private void initializer() {
        imageViewSplashLogo = findViewById(R.id.image_view_splash_logo);
        textViewSplash = findViewById(R.id.text_view_splash);
        rotateAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.alpha_anim);
        imageViewSplashLogo.setAnimation(rotateAnimation);
        translateAnimation = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.translate_anim);
        textViewSplash.setAnimation(translateAnimation);

    }

    private void removeStatusBar() {
//        These Lines Are Used To Remove Status Bar...
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void splashScreen() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(2000);
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
