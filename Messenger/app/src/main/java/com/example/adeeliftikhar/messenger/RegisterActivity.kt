package com.example.adeeliftikhar.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

//        Register Widgets Syntax...
        val name = edit_text_register_name.text.toString().trim()
        val email = edit_text_register_email.text.toString().trim()
        val password = edit_text_register_password.text.toString().trim()

//        This is the way to work with LogCats...
        Log.d("MainActivity", "Name is: $name")         // String Concatenation Syntax...
        Log.d("MainActivity", "Email is: $email")       // String Concatenation Syntax...
        Log.d("MainActivity", "Password is: $password") // String Concatenation Syntax...

//        clickListener Syntax...
        register_button.setOnClickListener {
            Toast.makeText(this,"Register Successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        text_view_have_account.setOnClickListener {
            //            Intent Syntax...
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
