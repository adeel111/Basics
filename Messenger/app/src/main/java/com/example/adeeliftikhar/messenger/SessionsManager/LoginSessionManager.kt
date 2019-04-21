package com.example.adeeliftikhar.messenger.SessionsManager

import android.content.Context
import android.content.SharedPreferences

class LoginSessionManager
//    every preference has its own type,
//    like private or public or specifically protected...
//    0 ==> stands for private mode of the preference.
//    1 ==> stands for public mode of the preference.

    (context: Context) {

    internal var editor: SharedPreferences.Editor
    internal var sharedPreferences: SharedPreferences
    internal var isLoggedIn: Boolean? = false

    init {
        sharedPreferences = context.getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun loginTheUser(isLoggedIn: Boolean, email: String, password: String) {
        editor.putBoolean("userLoginType", isLoggedIn)
        editor.putString("incomingEmail", email)
        editor.putString("incomingPassword", password)
        editor.commit()
    }

    //        Method to check weather the user is already logged in or not...
    fun checkUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("userLoginType", isLoggedIn!!)
    }
}