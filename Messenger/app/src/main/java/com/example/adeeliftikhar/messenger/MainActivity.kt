package com.example.adeeliftikhar.messenger

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.adeeliftikhar.messenger.Internet.CheckInternetConnectivity
import com.example.adeeliftikhar.messenger.SessionsManager.LoginSessionManager
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.action_bar_nav_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.new_message -> {
                if (!CheckInternetConnectivity.isConnected(this)) {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, NewMessageActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.logout -> {
                logoutAlertBox()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutAlertBox() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Logout")
        builder.setMessage("Do you want to Logout?")
        builder.setPositiveButton("Logout") { dialog, which ->
            if (!CheckInternetConnectivity.isConnected(this@MainActivity)) {
                Toast.makeText(this@MainActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                dialogBoxLogout()
                logout()
            }
        }.setNegativeButton("Cancel") { dialog, which -> }
        builder.show()
    }

    private fun dialogBoxLogout() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Logout in Progress.")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun logout() {
        val loginSessionManager = LoginSessionManager(this@MainActivity)
        loginSessionManager.loginTheUser(false, "", "")
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
