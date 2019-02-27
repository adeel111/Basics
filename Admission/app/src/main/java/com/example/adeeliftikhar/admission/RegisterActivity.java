package com.example.adeeliftikhar.admission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.Internet.CheckInternetConnectivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextRegisterName, editTextRegisterEmail, editTextRegisterPhoneNo,
            editTextRegisterAddress, editTextRegisterPassword;
    String incomingName, incomingEmail, incomingPhoneNo, incomingAddress, incomingPassword;
    TextView textViewGotoLoginPage;
    LinearLayout linearLayout;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        This method will initialize all the widgets...
        initializer();
    }

    private void initializer() {
        editTextRegisterName = findViewById(R.id.edit_text_register_name);
        editTextRegisterEmail = findViewById(R.id.edit_text_register_email);
        editTextRegisterPhoneNo = findViewById(R.id.edit_text_register_phone_no);
        editTextRegisterAddress = findViewById(R.id.edit_text_register_address);
        editTextRegisterPassword = findViewById(R.id.edit_text_register_password);
        textViewGotoLoginPage = findViewById(R.id.text_view_goto_login_page);
        linearLayout = findViewById(R.id.linear_layout);
        FirebaseApp.initializeApp(this);

//        Goto Login Page...
        textViewGotoLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public void buttonRegisterUser(View view) {
        boolean response = validateUserData();
        if (!response) {
            showSnackBar();
        } else {
            if (!CheckInternetConnectivity.isConnected(RegisterActivity.this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                showProgressDialog();
                signUp();
            }
        }
    }

    private boolean validateUserData() {
        incomingName = editTextRegisterName.getText().toString();
        incomingEmail = editTextRegisterEmail.getText().toString();
        incomingPhoneNo = editTextRegisterPhoneNo.getText().toString();
        incomingAddress = editTextRegisterAddress.getText().toString();
        incomingPassword = editTextRegisterPassword.getText().toString();
        if (incomingName.isEmpty() || incomingEmail.isEmpty() || incomingPhoneNo.isEmpty() || incomingAddress.isEmpty() || incomingPassword.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    private void showSnackBar() {
        Snackbar snackbar = Snackbar.make(linearLayout, "Please Fill All Fields", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"));
        snackbar.show();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Registration");
        progressDialog.setMessage("Registration in Progress, Plz Wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void signUp() {
        FirebaseApp.initializeApp(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
//        It will create the user with email and password.
        mAuth.createUserWithEmailAndPassword(incomingEmail, incomingPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Now Get Current User ID of Created User(that will always be in String Form).
                    String currentUserID = mAuth.getCurrentUser().getUid();
//                    Give the Current user id to dbRef so that values can store at that user place.
                    dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
//                    Table Structure will be created at run time and we will put values in it through HashMap...
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("name", incomingName);
                    hashMap.put("email", incomingEmail);
                    hashMap.put("phone_no", incomingPhoneNo);
                    hashMap.put("address", incomingAddress);
                    hashMap.put("password", incomingPassword);
//                    Put values in DB through dbRef which has the id of current user.
                    dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registered Successfully, Now Login", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                progressDialog.hide();
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
