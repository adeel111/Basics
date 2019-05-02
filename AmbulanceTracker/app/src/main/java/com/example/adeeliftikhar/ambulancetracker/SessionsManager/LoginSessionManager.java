package com.example.adeeliftikhar.ambulancetracker.SessionsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

import com.example.adeeliftikhar.ambulancetracker.MainActivity;
import com.example.adeeliftikhar.ambulancetracker.MainActivity;

public class LoginSessionManager {

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Boolean isLoggedIn = false;

    public LoginSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void loginTheUser(boolean isLoggedIn, String comingName, String comingNumber) {
        editor.putBoolean("userLoginType", isLoggedIn);
        editor.putString("incomingName", comingName);
        editor.putString("incomingNumber", comingNumber);
        editor.commit();
    }

    public String getName() {
        String name = sharedPreferences.getString("incomingName", "");
        return name;
    }

    public String getNumber() {
        String number = sharedPreferences.getString("incomingNumber", "");
        return number;
    }

    public void savePermanentValue(String comingValue) {
        editor.putString("incomingValue", comingValue);
        editor.commit();
    }

    public String getPermanentValue() {
        String permanentValue = sharedPreferences.getString("incomingValue", "");
        return permanentValue;
    }

    public boolean checkUserLoggedIn() {
        return sharedPreferences.getBoolean("userLoginType", isLoggedIn);
    }
}
