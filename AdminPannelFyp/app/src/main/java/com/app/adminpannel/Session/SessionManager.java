package com.app.adminpannel.Session;

import android.content.Context;
import android.content.SharedPreferences;


public class SessionManager {
    SharedPreferences mSharefPref;
    SharedPreferences.Editor editor;

    public static String phone,name;

    String mPrefName="SessionPref";
    boolean LOGGEDIN = false;

    public SessionManager(Context context) {
        mSharefPref=context.getSharedPreferences(mPrefName,Context.MODE_PRIVATE);
        editor=mSharefPref.edit();
    }

    public void logTheUserIn(boolean LOGGEDIN,String phone,String password){
        editor.putBoolean("userLoginType",LOGGEDIN);
        editor.putString("userPhone",phone);
        editor.putString("userPassword",password);
        editor.commit();
    }

    public boolean checkUserLogin(){
        return mSharefPref.getBoolean("userLoginType",false);
    }
}