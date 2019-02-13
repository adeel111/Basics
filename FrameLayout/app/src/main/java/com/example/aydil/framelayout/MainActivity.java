package com.example.aydil.framelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    ImageView flag1,flag2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flag1 = (ImageView) findViewById(R.id.flag1);
        flag2 = (ImageView) findViewById(R.id.flag2);

        flag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(flag1.getId()==R.id.flag1)
                flag1.setVisibility(View.GONE);
                flag2.setVisibility(View.VISIBLE);
            }
        });
        flag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag2.setVisibility(View.GONE);
                flag1.setVisibility(View.VISIBLE);
            }
        });
    }
}
