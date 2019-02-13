package com.example.adeeliftikhar.sqlitedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void gotoLoginPage(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void gotoRegistrationPage(View view) {
        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void gotoInsertStudent(View view) {
        Intent intent = new Intent(MainActivity.this, StudentActivity.class);
        startActivity(intent);
    }

    public void gotoUpdatePage(View view) {
        Intent intent = new Intent(MainActivity.this, UpdateStudentActivity.class);
        startActivity(intent);
    }

    public void gotoDeletePage(View view) {
        Intent intent1 = new Intent(MainActivity.this, DeleteActivity.class);
        startActivity(intent1);
    }

    public void gotoInsertImagePage(View view) {
        Intent intent1 = new Intent(MainActivity.this, UploadImageActivity.class);
        startActivity(intent1);
    }
}