package com.example.adeeliftikhar.ambulancetracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.SessionsManager.LoginSessionManager;

public class LoginActivity extends AppCompatActivity {

    LoginSessionManager loginSessionManager;
    EditText editTextName, editTextNumber;
    String name, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        It will initialize all the Widgets...
        initializer();
    }

    private void initializer() {
        editTextName = findViewById(R.id.edit_text_name);
        editTextNumber = findViewById(R.id.edit_text_number);
        loginSessionManager = new LoginSessionManager(LoginActivity.this);
        checkLoginSession();
    }

    private void checkLoginSession() {
        if (loginSessionManager.checkUserLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void buttonNumberLogin(View view) {
        name = editTextName.getText().toString().trim();
        number = editTextNumber.getText().toString().trim();
        Boolean response = validateData(name, number);
        if (response.equals(true)) {
            // converting String to StringBuilder
            StringBuilder builder = new StringBuilder(number);
            // removing first character
            builder.deleteCharAt(0);

            number = "+92" + builder;
//            Toast.makeText(this, "Number is " + number, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, VerifyPhoneNumberActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("number", number);
            startActivity(intent);
        }
    }

    private Boolean validateData(String name, String number) {
        if (name.isEmpty()) {
            editTextName.setError("Name is Required");
            editTextName.requestFocus();
            return false;
        }
        if (number.isEmpty()) {
            editTextNumber.setError("Number is Required");
            editTextNumber.requestFocus();
            return false;
        } else if (number.length() != 11) {
            Toast.makeText(this, "Number Should Contain 11 Digits", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}