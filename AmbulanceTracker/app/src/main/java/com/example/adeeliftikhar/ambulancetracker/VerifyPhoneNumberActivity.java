package com.example.adeeliftikhar.ambulancetracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.SessionsManager.LoginSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class VerifyPhoneNumberActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String comingName, comingNumber, verificationCode;
    private ProgressBar progressBar;

    private EditText codeDigitOne, codeDigitTwo, codeDigitThree, codeDigitFour, codeDigitFive, codeDigitSix;
    Button buttonPhoneLoginManually;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        initializer();

        getComingData();
    }

    private void initializer() {

        mAuth = FirebaseAuth.getInstance();

//        One
        codeDigitOne = findViewById(R.id.code_digit_one);
        codeDigitOne.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeDigitOne.getText().toString().length() == 1)     //size as per your requirement
                {
                    codeDigitTwo.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

//        Two
        codeDigitTwo = findViewById(R.id.code_digit_two);
        codeDigitTwo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeDigitTwo.getText().toString().length() == 1)     //size as per your requirement
                {
                    codeDigitThree.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

//        Three
        codeDigitThree = findViewById(R.id.code_digit_three);
        codeDigitThree.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeDigitThree.getText().toString().length() == 1)     //size as per your requirement
                {
                    codeDigitFour.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

//        Four
        codeDigitFour = findViewById(R.id.code_digit_four);
        codeDigitFour.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeDigitFour.getText().toString().length() == 1)     //size as per your requirement
                {
                    codeDigitFive.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

//        Five
        codeDigitFive = findViewById(R.id.code_digit_five);
        codeDigitFive.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeDigitFive.getText().toString().length() == 1)     //size as per your requirement
                {
                    codeDigitSix.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

//        Six

        codeDigitSix = findViewById(R.id.code_digit_six);
        codeDigitSix.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (codeDigitSix.getText().toString().length() == 1)     //size as per your requirement
                {
                    return;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

//        ClickListeners...
        buttonPhoneLoginManually = findViewById(R.id.button_phone_login_manually);
        buttonPhoneLoginManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginManually();
            }
        });
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.VISIBLE);
    }

    private void getComingData() {
        Intent intent = getIntent();
        comingName = intent.getStringExtra("name");
        comingNumber = intent.getStringExtra("number");

        sendVerificationCode(comingNumber);
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,     //     Time to resend code...
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                myCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks myCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String comingCode, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(comingCode, forceResendingToken);

            verificationCode = comingCode;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code == null) {
                Toast.makeText(VerifyPhoneNumberActivity.this, "This Number is already registered, Plz try another", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            } else {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VerifyPhoneNumberActivity.this, "Error ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    //    If auto-detection code not works...
    public void loginManually() {
        String code = codeDigitOne.getText().toString().trim() + codeDigitTwo.getText().toString().trim()
                + codeDigitThree.getText().toString().trim() + codeDigitFour.getText().toString().trim()
                + codeDigitFive.getText().toString().trim() + codeDigitSix.getText().toString().trim();

        if (codeDigitOne.getText().toString().isEmpty() || codeDigitTwo.getText().toString().isEmpty() || codeDigitThree.getText().toString().isEmpty()
                || codeDigitFour.getText().toString().isEmpty() || codeDigitFive.getText().toString().isEmpty() || codeDigitSix.getText().toString().isEmpty()) {
            Toast.makeText(this, "Plz Fill all Fields", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        }
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(verificationCode, code);
        signInManually(credentials);
    }

    private void signInManually(PhoneAuthCredential credentials) {
        mAuth.signInWithCredential(credentials).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    LoginSessionManager loginSessionManager = new LoginSessionManager(VerifyPhoneNumberActivity.this);
                    loginSessionManager.loginTheUser(true, comingName, comingNumber);
                    saveCredentialsToDB();
                } else {
                    Toast.makeText(VerifyPhoneNumberActivity.this, "Error ==> " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveCredentialsToDB() {
        String currentId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("PhoneNumberUsers").child(currentId);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", comingName);
        hashMap.put("number", comingNumber);
        dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(VerifyPhoneNumberActivity.this, MainActivity.class);
//                    This will start new activity by closing all other activities by clearing Stack...
                    intent.putExtra("name", comingName);
                    intent.putExtra("number", comingNumber);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(VerifyPhoneNumberActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
