package com.example.adeeliftikhar.ticktalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.ticktalk.Common.Common;
import com.example.adeeliftikhar.ticktalk.LoginSessionPkg.SessionManagerClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {
    EditText editTextLoginEmail, editTextLoginPassword;
    String loginEmail, loginPassword;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    SessionManagerClass mSessionManagerClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextLoginEmail = findViewById(R.id.edit_text_login_email);
        editTextLoginPassword = findViewById(R.id.edit_text_login_password);
        mSessionManagerClass = new SessionManagerClass(LoginActivity.this);
        if (mSessionManagerClass.checkUserLoggedIn() == true) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
//            Toast.makeText(this, "User was already Logged In", Toast.LENGTH_SHORT).show();
        }
    }

    public void login(View view) {
        Common.isConnected(LoginActivity.this);
        loginEmail = editTextLoginEmail.getText().toString();
        loginPassword = editTextLoginPassword.getText().toString();
        Boolean value = validData(loginEmail, loginPassword);
        if (!value) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog();
            login(loginEmail, loginPassword);
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Wait Till it Completed...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void login(final String loginEmail, final String loginPassword) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    mSessionManagerClass.loginTheUser(true, loginEmail, loginPassword);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    progressDialog.hide();
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(LoginActivity.this, "May be Network Error ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Boolean validData(String loginEmail, String loginPassword) {
        if (loginEmail.isEmpty() || loginPassword.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
            if (loginEmail.isEmpty()) {
                editTextLoginEmail.setError("Please Fill This Field");
            }
            if (loginPassword.isEmpty()) {
                editTextLoginPassword.setError("Please Fill This Field");
            }
            return false;
        } else {
            return true;
        }
    }

    public void gotoRegisterPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
