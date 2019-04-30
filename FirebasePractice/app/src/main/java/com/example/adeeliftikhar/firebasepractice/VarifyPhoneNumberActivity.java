package com.example.adeeliftikhar.firebasepractice;

import android.arch.core.executor.TaskExecutor;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

public class VarifyPhoneNumberActivity extends AppCompatActivity {

    private String comingNumber, verificationCode;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private EditText codeDigitOne, codeDigitTwo, codeDigitThree, codeDigitFour, codeDigitFive, codeDigitSix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varify_phone_number);

        initializer();

        getComingData();
    }

    private void initializer() {

        mAuth = FirebaseAuth.getInstance();

        codeDigitOne = findViewById(R.id.code_digit_one);
        codeDigitTwo = findViewById(R.id.code_digit_two);
        codeDigitThree = findViewById(R.id.code_digit_three);
        codeDigitFour = findViewById(R.id.code_digit_four);
        codeDigitFive = findViewById(R.id.code_digit_five);
        codeDigitSix = findViewById(R.id.code_digit_six);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void getComingData() {
        Intent intent = getIntent();
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
                progressBar.setVisibility(View.GONE);
                Toast.makeText(VarifyPhoneNumberActivity.this, "Enter Code Manually", Toast.LENGTH_SHORT).show();
            } else {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VarifyPhoneNumberActivity.this, "Error ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    //    If auto-detection code not works...
    public void Login(View view) {
        String code = codeDigitOne.getText().toString().trim() + codeDigitTwo.getText().toString().trim() + codeDigitThree.getText().toString().trim() + codeDigitFour.getText().toString().trim();
        Toast.makeText(this, "Code is " + code, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.VISIBLE);
        verifyCode(code);
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
                    Intent intent = new Intent(VarifyPhoneNumberActivity.this, SuccessActivity.class);
//                    This will start new activity by closing all other activities by clearing Stack...
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(VarifyPhoneNumberActivity.this, "Error ==> " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
