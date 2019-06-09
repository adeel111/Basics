package com.techndevs.displayactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.techndevs.displayactivity.Constants.Common;

public class LoginActivity extends AppCompatActivity {
    EditText edtTxtEamil,edtTxtPassword;
    String email,password;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initizalizations();

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loggin in");
        progressDialog.setMessage("Please wait a while");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtTxtEamil.getText().toString().trim();
                password = edtTxtPassword.getText().toString().trim();


                if(Common.isConnectedtoInternet(LoginActivity.this)){
                    if(email.isEmpty() && password.isEmpty()){
                        edtTxtEamil.setError("Please enter Email");
                        edtTxtPassword.setError("Please enter Password");
                        edtTxtEamil.requestFocus();
                    }else{
                        performSignUp();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Seems you're not connected to internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

        private void performSignUp() {
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,String.valueOf(task.getException()) , Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        private void initizalizations() {
            edtTxtEamil=findViewById(R.id.edt_txt_email);
            edtTxtPassword=findViewById(R.id.edt_txt_password);
            btnLogin = findViewById(R.id.btn_login);
        }

        public void onClickSignup(View view) {
            startActivity(new Intent(LoginActivity.this,SignupActivity.class));
            finish();
        }


        @Override
        protected void onStart() {
            super.onStart();
        }
}
