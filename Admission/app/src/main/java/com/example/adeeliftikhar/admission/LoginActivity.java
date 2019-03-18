package com.example.adeeliftikhar.admission;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.admission.SessionsManager.LoginSessionManager;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {
    EditText editTextLoginEmail, editTextLoginPassword;
    String incomingLoginEmail, incomingLoginPassword;
    CheckBox checkBoxRememberMe;
    LinearLayout linearLayout, linearLayoutSpinKit, linearLayoutMaterial, linearLayoutMaterialOne;
    FirebaseAuth mAuth;

    LoginSessionManager loginSessionManager;
    ProgressBar progressBar;
    ThreeBounce threeBounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        This method will initialize all the widgets...
        initializer();
    }

    private void initializer() {

        progressBar = findViewById(R.id.spin_kit_view);
        threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
//        progressBar.setVisibility(View.GONE);

        linearLayout = findViewById(R.id.linear_layout);
        linearLayoutSpinKit = findViewById(R.id.linear_layout_spin_kit);
        linearLayoutMaterial = findViewById(R.id.linear_layout_material);
        linearLayoutMaterialOne = findViewById(R.id.linear_layout_materialOne);

        linearLayoutSpinKit.setVisibility(View.GONE);

        editTextLoginEmail = findViewById(R.id.edit_text_login_email);
        editTextLoginPassword = findViewById(R.id.edit_text_login_password);
        checkBoxRememberMe = findViewById(R.id.checkbox_remember_me);
        loginSessionManager = new LoginSessionManager(LoginActivity.this);
        checkLoginSession();
    }

    private void checkLoginSession() {
        if (loginSessionManager.checkUserLoggedIn()) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        } else {
//            Toast.makeText(this, "User is Already Logged in", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonLoginUser(View view) {
//        How to use SpinKitView in Button...
//        Button buttonLogin = view.findViewById(R.id.button_login);
//        buttonLogin.setCompoundDrawables(null, null, fadingCircle, null);

        boolean response = validateTheData();
        if (!response) {
            showSnackBar();
        } else {
            if (!CheckInternetConnectivity.isConnected(LoginActivity.this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            } else {
                linearLayoutSpinKit.setVisibility(View.VISIBLE);
                linearLayoutMaterial.setVisibility(View.GONE);
                linearLayoutMaterialOne.setVisibility(View.GONE);
                if (checkBoxRememberMe.isChecked()) {
                    RememberLogin();
                } else {
                    simpleLogin();
                }
            }
        }
    }

    private boolean validateTheData() {
        incomingLoginEmail = editTextLoginEmail.getText().toString();
        incomingLoginPassword = editTextLoginPassword.getText().toString();
        if (incomingLoginEmail.isEmpty() || incomingLoginPassword.isEmpty()) {
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

    private void RememberLogin() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(incomingLoginEmail, incomingLoginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginSessionManager.loginTheUser(true, incomingLoginEmail, incomingLoginPassword);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    linearLayoutSpinKit.setVisibility(View.GONE);
                    linearLayoutMaterial.setVisibility(View.VISIBLE);
                    linearLayoutMaterialOne.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void simpleLogin() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(incomingLoginEmail, incomingLoginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    linearLayoutSpinKit.setVisibility(View.GONE);
                    linearLayoutMaterial.setVisibility(View.VISIBLE);
                    linearLayoutMaterialOne.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoRegisterPage(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
