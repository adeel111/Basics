package com.example.adeeliftikhar.sqlitedb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.sqlitedb.Database.DatabaseOperations;

public class RegistrationActivity extends AppCompatActivity {
    DatabaseOperations databaseOperationsObject;
    SQLiteDatabase sqLiteDatabase;
    EditText editTextName, editTextEmail, editTextPassword, editTextPhoneNumber, editTextAddress;
    String incomingName, incomingEmail, incomingPassword, incomingPhoneNumber, incomingAddress;
    Button buttonSignUp, buttonShowUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        databaseOperationsObject = new DatabaseOperations(RegistrationActivity.this);
        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextAddress = findViewById(R.id.edit_text_address);
        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonShowUserData = findViewById(R.id.button_show_user_data);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              We are taking data from Edit Text fields and converting it into String.
                incomingName = editTextName.getText().toString();
                incomingEmail = editTextEmail.getText().toString();
                incomingPassword = editTextPassword.getText().toString();
                incomingPhoneNumber = editTextPhoneNumber.getText().toString();
                incomingAddress = editTextAddress.getText().toString();
//                Following Condition will check all fields...
                if (incomingName.isEmpty() || incomingEmail.isEmpty() || incomingPassword.isEmpty() ||
                        incomingPhoneNumber.isEmpty() || incomingAddress.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
                    if (incomingName.isEmpty()) {
                        editTextName.setError("Please Fill This Field");
                    }
                    if (incomingEmail.isEmpty()) {
                        editTextEmail.setError("Please Fill This Field");
                    }
                    if (incomingPassword.isEmpty()) {
                        editTextPassword.setError("Please Fill This Field");
                    }
                    if (incomingPhoneNumber.isEmpty()) {
                        editTextPhoneNumber.setError("Please Fill This Field");
                    }
                    if (incomingAddress.isEmpty()) {
                        editTextAddress.setError("Please Fill This Field");
                    }
                }
//                    Now we are going to enter values in Database because form is now Completely Filled.
                else {
//                    getWritableDatabase() ==> This method is used to write data in Database...
                    sqLiteDatabase = databaseOperationsObject.getWritableDatabase();
                    databaseOperationsObject.insertNewUser(incomingName, incomingEmail, incomingPassword, incomingPhoneNumber, incomingAddress, sqLiteDatabase);
                    Toast.makeText(RegistrationActivity.this, "Values are Entered in Database", Toast.LENGTH_SHORT).show();
                    editTextName.setText("");
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    editTextPhoneNumber.setText("");
                    editTextAddress.setText("");
                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        buttonShowUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase = databaseOperationsObject.getReadableDatabase();
                Cursor cursor = databaseOperationsObject.getUserDataFromDatabase(sqLiteDatabase);
                if (cursor.moveToFirst()) {
                    do {
                        String userName = cursor.getString(0);
                        String userEmail = cursor.getString(1);
                        String userPassword = cursor.getString(2);
                        String userPhoneNumber = cursor.getString(3);
                        String userAddress = cursor.getString(4);
                        Toast.makeText(RegistrationActivity.this, userName + ", " + userEmail + ", " + userPassword + ", " + userPhoneNumber + ", " + userAddress, Toast.LENGTH_SHORT).show();
                    } while (cursor.moveToNext());
                }
            }
        });
    }
}