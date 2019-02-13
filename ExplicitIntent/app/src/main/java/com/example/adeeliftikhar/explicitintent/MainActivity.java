package com.example.adeeliftikhar.explicitintent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button buttonSendData, buttonSendValues;
    EditText editTextData, editTextFirstValue, editTextSecondValue;
    TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonSendData = findViewById(R.id.button_send_data);
        buttonSendValues = findViewById(R.id.button_send_values);
        editTextData = findViewById(R.id.edit_text_data);
        editTextFirstValue = findViewById(R.id.edit_text_first_value);
        editTextSecondValue = findViewById(R.id.edit_text_second_value);
        textViewResult = findViewById(R.id.text_view_result);
    }

    public void buttonSendData(View view) {
        String text = editTextData.getText().toString();
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//        putExtra() => Used to send data...
        intent.putExtra("id", text);
        startActivity(intent);
    }

    public void buttonSendValues(View view) {
        if (editTextFirstValue.getText().toString().equals("") || editTextSecondValue.getText().toString().equals("")) {
            Toast.makeText(this, "Please enter Values", Toast.LENGTH_SHORT).show();
        } else {
            int number1 = Integer.parseInt(editTextFirstValue.getText().toString());
            int number2 = Integer.parseInt(editTextSecondValue.getText().toString());
            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            i.putExtra("number1", number1);
            i.putExtra("number2", number2);
            startActivityForResult(i, 1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                int result = data.getIntExtra("result", 0);
                textViewResult.setText("" + result);
            }
            if(resultCode == RESULT_CANCELED){
                textViewResult.setText("Nothing Selected");
            }
        }
    }
}
