package com.example.adeeliftikhar.mysqldatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText signInEmail, signInPassword;
    private Button buttonLoginUser, buttonShowLoginUser;
    String incomingSignInEmail, incomingSignInPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInEmail = findViewById(R.id.sign_in_email);
        signInPassword = findViewById(R.id.sign_in_password);
        buttonLoginUser = findViewById(R.id.button_log_in_user);
        buttonShowLoginUser = findViewById(R.id.button_show_login_user);
        buttonLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingSignInEmail = signInEmail.getText().toString();
                incomingSignInPassword = signInPassword.getText().toString();
                if (incomingSignInEmail.isEmpty() || incomingSignInPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
                    if (incomingSignInEmail.isEmpty()) {
                        signInEmail.setError("Please Fill This Field");
                    }
                    if (incomingSignInPassword.isEmpty()) {
                        signInPassword.setError("Please Fill This Field");
                    }
                } else {
                    return;
                }
            }
        });
    }
}
