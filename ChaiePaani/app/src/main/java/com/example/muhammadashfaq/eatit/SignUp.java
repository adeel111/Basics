package com.example.muhammadashfaq.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    EditText edtTxtPhone,edtTxtName,edtTxtPassword;
    Button btnSignup;
    DatabaseReference table_user;

    String phone,name,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtTxtPhone=findViewById(R.id.edt_txt_phone);
        edtTxtName=findViewById(R.id.edt_txt_name);
        edtTxtPassword=findViewById(R.id.edt_txt_password);

        btnSignup=findViewById(R.id.btn_signup);

        //Firebase init
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        table_user=firebaseDatabase.getReference("User");
        table_user.keepSynced(true);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone=edtTxtPhone.getText().toString();
                name=edtTxtName.getText().toString();
                password=edtTxtPassword.getText().toString();
                boolean val=validateUser(name,phone,password);


                if(val){
                    if(Common.isConnectedtoInternet(getBaseContext())){
                        signUpNewUser();
                    }else {
                        Snackbar.make(v,"Please Check Your Internet Connection",Snackbar.LENGTH_SHORT).show();
                    }
                }else{
                    Snackbar.make(v,"Please Fill All Fields",Snackbar.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void signUpNewUser() {
        if (Common.isConnectedtoInternet(getBaseContext())) {

            if(!edtTxtPhone.getText().toString().isEmpty() && !edtTxtName.getText().toString().isEmpty()
                    && !edtTxtPassword.getText().toString().isEmpty()) {

                //Progress Dailog
                final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Creating you Account");
                progressDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Check if user already exits phone number
                        progressDialog.dismiss();
                        UserModel userModel = new UserModel(edtTxtName.getText().toString(), edtTxtPassword.getText().toString(),edtTxtPhone.getText().toString());
                        table_user.child(edtTxtPhone.getText().toString()).setValue(userModel);
                        Intent homeintent = new Intent(SignUp.this, SigninActivity.class);
                        Common.currentUser = userModel;
                        Common.password=password;
                        Common.phone=edtTxtPhone.getText().toString();
                        Common.username=edtTxtName.getText().toString();
                        startActivity(homeintent);
                        customToast(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }else{
                Toast.makeText(SignUp.this, "Please enter all fields fields", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(SignUp.this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean validateUser(String userName, String userEmail, String userPassword) {
        if(userName.isEmpty() && userEmail.isEmpty() && userPassword.isEmpty()){
            edtTxtName.setError("Please Enter Name ");
            edtTxtPhone.setError("Please Enter Phone Number ");
            edtTxtPassword.setError("Please Enter Password ");
            return false;
        }else if(userName.isEmpty()){
            edtTxtName.setError("Please enter Name ");
            return false;
        }else if(userEmail.isEmpty()){
            edtTxtPhone.setError("Please enter Phone ");
            return false;
        }else if(userPassword.isEmpty()){
            edtTxtPassword.setError("Please enter Password ");
            return false;
        } else if(userName.isEmpty() && !userEmail.isEmpty() && !userPassword.isEmpty()){
            edtTxtName.setError("Please enter Name ");
            return false;
        }else if(!userName.isEmpty() && userEmail.isEmpty() && !userPassword.isEmpty()){
            edtTxtPhone.setError("Please enter Phone ");
            return false;
        }else if(!userName.isEmpty() && !userEmail.isEmpty() && userPassword.isEmpty()){
            edtTxtPassword.setError("Please enter Password ");
            return false;
        }else {
            return true;
        }
    }
    public void customToast(boolean b)
    {
        View customToastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup)findViewById(R.id.custom_toast));
        TextView textViewToast=customToastView.findViewById(R.id.custom_toast_txt_vu);
        if(b){
            textViewToast.setText("Please Login to proceed");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();
        }else
        {
            textViewToast.setText("Phone Number already exits.Try another one");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();

        }
    }
}
