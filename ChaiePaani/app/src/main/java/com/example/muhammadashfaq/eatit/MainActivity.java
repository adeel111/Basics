package com.example.muhammadashfaq.eatit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Model.UserModel;
import com.example.muhammadashfaq.eatit.SessionManager.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.BatchUpdateException;

import io.paperdb.Paper;


public class MainActivity extends AppCompatActivity {

    Button btnSignup, btnSignin;

    TextView txtVuSologon;

    SessionManager mSessionManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSessionManger=new SessionManager(this);
        boolean isUserLogedIn=mSessionManger.checkUserLogin();
        if(isUserLogedIn){

            UserModel userModel = new UserModel(Common.username, Common.password,Common.phone);
            Common.currentUser = userModel;
            Common.username=SessionManager.name;
            Common.phone=SessionManager.phone;
           /* Toast.makeText(this, Common.phone+Common.username, Toast.LENGTH_SHORT).show();*/
            Intent intent=new Intent(MainActivity.this,Home.class);
            startActivity(intent);
            finish();

        }else{

        }

        btnSignup = findViewById(R.id.btn_signup);
        btnSignin = findViewById(R.id.btn_signin);

        txtVuSologon = findViewById(R.id.txt_vu_sologon);

        //Applying  NABILA font to text
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/NABILA.TTF");
        txtVuSologon.setTypeface(typeface);

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });


    }

    @SuppressLint("ResourceType")
    private void login(final String phone, final String password) {

        //Firebase init
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");


        if (Common.isConnectedtoInternet(getBaseContext())) {

            //Progress Dailog
            final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading");
            progressDialog.show();

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //check if user not exit in database
                    if (dataSnapshot.child(phone).exists()) {


                        progressDialog.dismiss();
                        //Getting user information
                        UserModel userModel = dataSnapshot.child(phone).getValue(UserModel.class);
                        userModel.setPhone(phone);
                        if (userModel.getPassword().equals(password)){
                            Intent homeintent = new Intent(MainActivity.this, Home.class);
                            //Saving current user
                            Common.currentUser = userModel;
                            startActivity(homeintent);
                            finish();
                            customToast(true);
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

        } else {
            customToast(false);
            return;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void customToast(boolean b) {
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
