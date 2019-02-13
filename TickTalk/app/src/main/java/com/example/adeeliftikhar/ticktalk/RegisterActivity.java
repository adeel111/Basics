package com.example.adeeliftikhar.ticktalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    EditText editTextRegisterName, editTextRegisterEmail, editTextRegisterPassword, editTextRegisterReEnterPassword;
    String regName, regEmail, regPassword, regRePassword;
    Button buttonRegister;
    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    ProgressDialog progressDialog;
    FirebaseApp firebaseApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextRegisterName = findViewById(R.id.edit_text_register_name);
        editTextRegisterEmail = findViewById(R.id.edit_text_register_email);
        editTextRegisterPassword = findViewById(R.id.edit_text_register_password);
        editTextRegisterReEnterPassword = findViewById(R.id.edit_text_register_re_enter_password);
        buttonRegister = findViewById(R.id.button_register);
        firebaseApp = FirebaseApp.initializeApp(this);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regName = editTextRegisterName.getText().toString();
                regEmail = editTextRegisterEmail.getText().toString();
                regPassword = editTextRegisterPassword.getText().toString();
                regRePassword = editTextRegisterReEnterPassword.getText().toString();
                Boolean value = validateUser(regName, regEmail, regPassword, regRePassword);
                if (!value) {
                    Toast.makeText(RegisterActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                } else {
                    showProgressDialog();
                    singUp(regName, regEmail, regPassword);
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("Wait Till it Completed...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private boolean validateUser(String regName, String regEmail, String regPassword, String regRePassword) {
        if (regName.isEmpty() || regEmail.isEmpty() || regPassword.isEmpty() ||
                regRePassword.isEmpty() || (!regPassword.equals(regRePassword))) {
//            Toast.makeText(RegisterActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
            if (regName.isEmpty()) {
                editTextRegisterName.setError("Please Fill This Field");
            }
            if (regEmail.isEmpty()) {
                editTextRegisterEmail.setError("Please Fill This Field");
            }
            if (regPassword.isEmpty()) {
                editTextRegisterPassword.setError("Please Fill This Field");
            }
            if (regRePassword.isEmpty()) {
                editTextRegisterReEnterPassword.setError("Please Fill This Field");
            }
            if (!regPassword.equals(regRePassword)) {
                Toast.makeText(this, "Passwords are not Matching", Toast.LENGTH_SHORT).show();
//                    editTextRegisterReEnterPassword.setError("Passwords are not Matching");
            }
            return false;
        } else {
            return true;
        }
    }

    private void singUp(final String regName, String regEmail, String regPassword) {
        FirebaseApp.initializeApp(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(regEmail, regPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

//                    First way to get Current User ID (that will always be in String Form).
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String currentUserId = firebaseUser.getUid();

//                    Second way to get Current User ID.
//                    String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

//                    Third way to get Current User ID.
//                    String currentUserID = mAuth.getCurrentUser().getUid();

                    dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);

                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("name", regName);
                    hashMap.put("status", "Using App");
                    hashMap.put("image", "Default");
                    hashMap.put("thumb_image", "Default");
//                    Toast.makeText(RegisterActivity.this, hashMap.toString(), Toast.LENGTH_SHORT).show();
                    dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(RegisterActivity.this, "Login Now", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    progressDialog.hide();
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(RegisterActivity.this, "Can not sign in, Enter Valid Data" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoLoginPage(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
