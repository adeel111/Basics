package com.app.driverpannel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.driverpannel.Constants.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText edtTxtEamil,edtTxtPassword,edtTxtName,edtTxtPhone;
    String email,password,name,phone;

    Button btnSignUp;
    FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initizalizations();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Signing up");
        progressDialog.setMessage("Please wait a while");
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtTxtEamil.getText().toString().trim();
                password = edtTxtPassword.getText().toString().trim();
                name = edtTxtName.getText().toString().trim();
                phone= edtTxtPhone.getText().toString().trim();


                if(Constants.isConnectedtoInternet(MainActivity.this)){
                    if(email.isEmpty() && password.isEmpty() && name.isEmpty() && phone.isEmpty()){
                        edtTxtEamil.setError("Please enter Email");
                        edtTxtPassword.setError("Please enter Password");
                        edtTxtName.setError("Please enter Name");
                        edtTxtPhone.setError("Please enter Phone Number");
                        edtTxtEamil.requestFocus();
                    }else{
                        performSignUp();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Seems you're not connected to internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void performSignUp() {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    saveDataToDb();
                }

            }
        });

    }

    private void saveDataToDb() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("DriverInfo");
        String currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("email",email);
        hashMap.put("password",password);
        hashMap.put("name",name);
        hashMap.put("phone",phone);

        dbRef.child(currentUID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             if(task.isSuccessful()){
                 Toast.makeText(MainActivity.this, "DataSaved", Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
                 startActivity(new Intent(MainActivity.this,LoginActivity.class));
             }
            }
        });
    }

    private void initizalizations() {
        edtTxtEamil=findViewById(R.id.edt_txt_email);
        edtTxtPassword=findViewById(R.id.edt_txt_password);
        edtTxtPhone=findViewById(R.id.edt_txt_phone);
        edtTxtName=findViewById(R.id.edt_txt_name);
        btnSignUp = findViewById(R.id.btn_signup);
    }

    public void onClickLogin(View view) {
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }
}
