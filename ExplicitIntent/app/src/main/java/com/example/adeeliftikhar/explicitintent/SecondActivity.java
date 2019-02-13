package com.example.adeeliftikhar.explicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    TextView textViewValueReceive;
    TextView textViewValuessReceive;
    Button buttonAdd, buttonSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textViewValueReceive = findViewById(R.id.text_view_value_receive);
        textViewValuessReceive = findViewById(R.id.text_view_valuess_receive);
        buttonAdd = findViewById(R.id.button_add);
        buttonSub = findViewById(R.id.button_sub);
        Intent intent = getIntent();
        String data = intent.getStringExtra("id");
        textViewValueReceive.setText(data);

        Intent intent1 = getIntent();
        final int number1 = intent1.getIntExtra("number1", 0);
        final int number2 = intent1.getIntExtra("number2", 0);
        textViewValuessReceive.setText("Numbers: " + number1 + ", " + number2);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result1 = number1 + number2;
                Intent myIntent = new Intent();
                myIntent.putExtra("result", result1);
                setResult(RESULT_OK, myIntent);
                finish();
            }
        });
        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result1 = number1 - number2;
                Intent myIntent = new Intent();
                myIntent.putExtra("result", result1);
                setResult(RESULT_OK, myIntent);
                finish();
            }
        });
    }
}