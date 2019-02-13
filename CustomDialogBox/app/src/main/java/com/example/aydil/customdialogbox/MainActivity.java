package com.example.aydil.customdialogbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements DialogBoxActivity.DialogBoxActivityListener {
    private TextView userName1;
    private TextView userPass;
    private Button dialogBoxButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName1 = (TextView) findViewById(R.id.user_name1);
        userPass = (TextView) findViewById(R.id.user_pass);
        dialogBoxButton = findViewById(R.id.dialog_box_button);
        dialogBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogBox();
            }
        });
    }

    public void openDialogBox() {
        DialogBoxActivity dialogBoxActivity = new DialogBoxActivity();
        dialogBoxActivity.show(getSupportFragmentManager(), "Example Dialog");
    }

    @Override
    public void applyTexts(String userName, String userPassword) {
//        Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
        userName1.setText(userName);
        userPass.setText(userPassword);
    }
}
