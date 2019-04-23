package com.example.adeeliftikhar.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.adeeliftikhar.messenger.Internet.CheckInternetConnectivity
import com.example.adeeliftikhar.messenger.SessionsManager.LoginSessionManager
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.title = "Login Here"

        val progressBar = spin_kit_view_login
        val threeBounce = ThreeBounce()
        progressBar.setIndeterminateDrawable(threeBounce)
        linear_layout_spin_kit_login.visibility = View.GONE     // Visibility Syntax...

        login_button.setOnClickListener {
            val checkBoxRememberMe = checkbox_remember_me
            if (checkBoxRememberMe.isChecked) {
                RememberLogin()
            } else {
                simpleLogin()
            }
        }

        text_view_have_account.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        To check either user is already login or not...
        checkLoginSession()
    }

    private fun checkLoginSession() {

        val loginSessionManager = LoginSessionManager(this@LoginActivity)
        if (loginSessionManager.checkUserLoggedIn()) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun RememberLogin() {

        val email = edit_text_login_email.text.toString().trim()
        val password = edit_text_login_password.text.toString().trim()
//            Validating Data...
        if (email.isEmpty() || password.isEmpty()) {
            showSnackBar()
        } else {
//            Checking Internet Connectivity...
            if (!CheckInternetConnectivity.isConnected(this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                linear_layout_spin_kit_login.visibility = View.VISIBLE

//            Firebase Authentication...
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val loginSessionManager = LoginSessionManager(this)
                        loginSessionManager.loginTheUser(true, email, password)
                        Log.d("Login", "Login Successfully with id ${it.result?.user?.uid}")
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        linear_layout_spin_kit_login.visibility = View.GONE
                    }
                }
                    .addOnFailureListener {
                        Log.d("Register", "Failed to create user:  ${it.message}")
                        Toast.makeText(this, "Failed to Login:  ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun simpleLogin() {

        val email = edit_text_login_email.text.toString().trim()
        val password = edit_text_login_password.text.toString().trim()
//            Validating Data...
        if (email.isEmpty() || password.isEmpty()) {
            showSnackBar()
        } else {
//            Checking Internet Connectivity...
            if (!CheckInternetConnectivity.isConnected(this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                linear_layout_spin_kit_login.visibility = View.VISIBLE

//            Firebase Authentication...
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("Login", "Login Successfully with id ${it.result?.user?.uid}")
                        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        linear_layout_spin_kit_login.visibility = View.GONE
                    }
                }
                    .addOnFailureListener {
                        Log.d("Register", "Failed to create user:  ${it.message}")
                        Toast.makeText(this, "Failed to Login:  ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun showSnackBar() {
        val linearLayout = linear_layout_login
        val snackbar = Snackbar.make(linearLayout, "Please Fill All Fields", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.WHITE).setAction("Ok") { }
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"))
        snackbar.show()
    }
}
