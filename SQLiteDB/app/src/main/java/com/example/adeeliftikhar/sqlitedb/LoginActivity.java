package com.example.adeeliftikhar.sqlitedb;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.sqlitedb.Database.DatabaseOperations;

public class LoginActivity extends AppCompatActivity {
    DatabaseOperations databaseOperationsObject;
    SQLiteDatabase sqLiteDatabase;
    private EditText signInEmail, signInPassword;
    private Button buttonLoginUser;
    String incomingSignInEmail, incomingSignInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseOperationsObject = new DatabaseOperations(LoginActivity.this);
        signInEmail = findViewById(R.id.sign_in_email);
        signInPassword = findViewById(R.id.sign_in_password);
        buttonLoginUser = findViewById(R.id.button_log_in_user);
        buttonLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingSignInEmail = signInEmail.getText().toString();
                incomingSignInPassword = signInPassword.getText().toString();
                if (incomingSignInEmail.isEmpty() || incomingSignInPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
                    if (incomingSignInEmail.isEmpty()) {
                        signInEmail.setError("Please Fill This Field");
                    }
                    if (incomingSignInPassword.isEmpty()) {
                        signInPassword.setError("Please Fill This Field");
                    }
                } else {
//              Check from Database is user is already registered or not...
                    sqLiteDatabase = databaseOperationsObject.getWritableDatabase();
                    boolean incomingSignInResponse = databaseOperationsObject.checkUserSignIn(incomingSignInEmail, incomingSignInPassword, sqLiteDatabase);
                    if (incomingSignInResponse == true) {
                        Toast.makeText(LoginActivity.this, "User is valid.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "User is Not Registered.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}