package com.example.adeeliftikhar.admission.SessionsManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

import com.example.adeeliftikhar.admission.LoginActivity;
import com.example.adeeliftikhar.admission.MainActivity;

public class LoginSessionManager {
//    In android, Session (Specific period of Time for an action) Management is a process which is
//    used to maintain the required values in a session to use it in application. Generally, in android
//    we can manage the logged in user details in session either by storing it in global variables or in
//    Shared Preferences.

//    Android provides many ways of storing data of an application. One of this way is called Shared
//    Preferences. Shared Preferences allow you to save and retrieve data in the form of key,value pair.

//    SharedPreferences.Editor. Interface used for modifying values in a SharedPreferences object. All
//    changes you make in an editor are batched, and not copied back to the original SharedPreferences
//    until you call commit() or apply().

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;
    Boolean isLoggedIn = false;

//    every preference has its own type,
//    like private or public or specifically protected...
//    0 ==> stands for private mode of the preference.
//    1 ==> stands for public mode of the preference.

    public LoginSessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void loginTheUser(boolean isLoggedIn, String email, String password) {
        editor.putBoolean("userLoginType", isLoggedIn);
        editor.putString("incomingEmail", email);
        editor.putString("incomingPassword", password);
        editor.commit();
    }

//        Method to check weather the user is already logged in or not...
    public boolean checkUserLoggedIn() {
        return sharedPreferences.getBoolean("userLoginType", isLoggedIn);
    }
}