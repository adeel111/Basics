package com.example.muhammadashfaq.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.SingleLineTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Model.UserModel;
import com.example.muhammadashfaq.eatit.SessionManager.SessionManager;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class SigninActivity extends AppCompatActivity {

    MaterialEditText edtTxtPhone,edtTxtPassword;
    Button btnSignin;
    CheckBox checkBoxRemember;

    SessionManager mSessionManager;

    String phone,password;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtTxtPhone=findViewById(R.id.edt_txt_phone);
        edtTxtPassword=findViewById(R.id.edt_txt_password);
        btnSignin=findViewById(R.id.btn_sign_in);
        checkBoxRemember=findViewById(R.id.check_box_remember);

        mSessionManager=new SessionManager(this);

       //Firebase init
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        table_user=firebaseDatabase.getReference("User");
        table_user.keepSynced(true);

        //btn click listner
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone = edtTxtPhone.getText().toString();
                password = edtTxtPassword.getText().toString();
                boolean val = validateLogin(phone, password);
                if (val) {
                    if (Common.isConnectedtoInternet(getBaseContext())) {
                        loginUser();
                    }else {
                        Snackbar.make(v,"Please Check Your Internet Connection",Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    Snackbar.make(v,"Please Fill All Fields",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void loginUser() {
            final ProgressDialog progressDialog = new ProgressDialog(SigninActivity.this);
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("We're Checking your Credentiols...");
            progressDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //check if user not exit in database
                    if (dataSnapshot.child(edtTxtPhone.getText().toString()).exists()) {


                        progressDialog.dismiss();
                        //Getting user information
                        UserModel userModel = dataSnapshot.child(edtTxtPhone.getText().toString()).getValue(UserModel.class);
                        userModel.setPhone(edtTxtPhone.getText().toString());
                        if (userModel.getPassword().equals(edtTxtPassword.getText().toString())) {
                            String phone=edtTxtPhone.getText().toString();
                            String password=edtTxtPassword.getText().toString();

                            if(checkBoxRemember.isChecked()){
                                Common.currentUser = userModel;
                                Common.password=password;
                                Common.phone=userModel.getPhone();
                                Common.username=userModel.getName();
                                mSessionManager.logTheUserIn(true,phone,password);
                            }else{
                                Common.password=password;
                                Common.phone=userModel.getPhone();
                                Common.username=userModel.getName();
                            }

                            Intent homeintent = new Intent(SigninActivity.this, Home.class);
                            startActivity(homeintent);
                            finish();
                            customToast(true);
                        } else {
                            customToast(false);
                        }
                    } else {
                        progressDialog.dismiss();
                        customToast(false);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    private boolean validateLogin(String email, String password) {
        if (email.isEmpty() && password.isEmpty()) {
            edtTxtPhone.setError("Please enter your Phone Number");
            edtTxtPassword.setError("Please enter your Password");
            return false;
        } else if (email.isEmpty()) {
            edtTxtPhone.setError("Please enter your Phone Number");
            return false;
        } else if (password.isEmpty()) {
            edtTxtPassword.setError("Please enter your Password");
            return false;
        } else if (!email.isEmpty() && password.isEmpty()) {
            edtTxtPassword.setError("Please enter your Password");
        } else if (email.isEmpty() && !password.isEmpty()) {
            edtTxtPhone.setError("Please enter your Phone Number");
            return false;
        }
        return true;
    }
    public void customToast(boolean b)
    {
        View customToastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup)findViewById(R.id.custom_toast));
        TextView textViewToast=customToastView.findViewById(R.id.custom_toast_txt_vu);
        if(b){
            textViewToast.setText("Cheers! You're logged in.");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();
        }else
        {
            textViewToast.setText("Sorry! Incorrect Phone or Password");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();

        }
    }
    public void customToastForConnection(boolean b) {
        View customToastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.custom_toast));
        TextView textViewToast = customToastView.findViewById(R.id.custom_toast_txt_vu);
        if (b) {
            textViewToast.setText("Online");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.setView(customToastView);
            toast.show();
        } else {
            textViewToast.setText("No Internet Connection");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 0);
            toast.setView(customToastView);
            toast.show();

        }
    }
}


