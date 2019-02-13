package com.example.aydil.animationsandsplashscreen;

import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

//    ImageView imgView;

    Button alphaButton;
    Button scaleButton;
    Button rotateButton;
    Button translateButton;
    Button transitionButton;

    Animation alphaAnimation;
    Animation scaleAnimation;
    Animation rotateAnimation;
    Animation translateAnimation;
    Animation transitionAnimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        imgView = (ImageView) findViewById(R.id.img_view);

        alphaButton = (Button) findViewById(R.id.alpha_button);
        scaleButton = (Button) findViewById(R.id.scale_button);
        rotateButton = (Button) findViewById(R.id.rotate_button);
        translateButton = (Button) findViewById(R.id.translate_button);
        transitionButton = (Button) findViewById(R.id.transition_button);

        alphaAnimation = AnimationUtils.loadAnimation(MainActivity.this,R.anim.alpha_anim);
        scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale_anim);
        rotateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate_anim);
        translateAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_anim);
        transitionAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.translate_anim);

        alphaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
//                anim.setDuration(3000);
                alphaButton.startAnimation(alphaAnimation);
            }
        });
        scaleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ScaleAnimation anim = new ScaleAnimation(1.0f, 3.0f,1.0f,3.0f);
//                anim.setDuration(3000);
                scaleButton.startAnimation(scaleAnimation);
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ScaleAnimation anim = new ScaleAnimation(1.0f, 3.0f,1.0f,3.0f);
//                anim.setDuration(3000);
                rotateButton.startAnimation(rotateAnimation);
            }
        });
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TranslateAnimation anim = new TranslateAnimation(1.0f, 3.0f,1.0f,3.0f);
//                anim.setDuration(3000);
                translateButton.startAnimation(translateAnimation);
            }
        });
        transitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transitionButton.startAnimation(transitionAnimation);
//                {(TransitionDrawable) imgView.getDrawable()}.startTransition(3000);
            }
        });
    }
}