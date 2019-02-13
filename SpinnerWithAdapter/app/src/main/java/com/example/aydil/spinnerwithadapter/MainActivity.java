package com.example.aydil.spinnerwithadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aydil.spinnerwithadapter.Adapter.CustomAdapter;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    CustomAdapter customAdapter;
    int flagPics[] = {
            R.drawable.pakistan,
            R.drawable.china,
            R.drawable.iran,
            R.drawable.iraq,
            R.drawable.australia,
            R.drawable.england,
            R.drawable.brazil,
    };

    //   String [] countryNames = {"Pakistan", "China", "Iran", "Iraq", "Australia", "England", "Brazil"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        customAdapter = new CustomAdapter(MainActivity.this, flagPics);
        spinner.setAdapter(customAdapter);
    }
}