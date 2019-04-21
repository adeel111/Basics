package com.example.adeeliftikhar.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.adeeliftikhar.messenger.Internet.CheckInternetConnectivity
import com.example.adeeliftikhar.messenger.Models.RegisterModel
import com.github.ybq.android.spinkit.style.ThreeBounce
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private var name: String = ""
    private var email: String = ""
    private var password: String = ""
    private var selectedPhotoURI: Uri? = null
    private var downloadImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        FirebaseApp.initializeApp(this)

        val progressBar = spin_kit_view_register
        val threeBounce = ThreeBounce()
        progressBar.setIndeterminateDrawable(threeBounce)
        linear_layout_spin_kit_register.visibility = View.GONE     // Visibility Syntax...

//        This is the way to work with LogCats...
//        Log.d("MainActivity", "Name is: $name")         // String Concatenation Syntax...
//        Log.d("MainActivity", "Email is: $email")       // String Concatenation Syntax...
//        Log.d("MainActivity", "Password is: $password") // String Concatenation Syntax...

//        clickListener Syntax...
        register_button.setOnClickListener {
            performRegistration()
        }

        register_circle_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        text_view_have_account.setOnClickListener {
            //            Intent Syntax...
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {

            selectedPhotoURI = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoURI)
            val bitmapDrawable = BitmapDrawable(bitmap)
            register_circle_image.setImageDrawable(bitmapDrawable)
        }
    }

    private fun performRegistration() {
//            Validating Data...
        name = edit_text_register_name.text.toString().trim()
        email = edit_text_register_email.text.toString().trim()
        password = edit_text_register_password.text.toString().trim()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showSnackBar()
        } else {
//            Checking Internet Connectivity...
            if (!CheckInternetConnectivity.isConnected(this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                linear_layout_spin_kit_register.visibility = View.VISIBLE

//            Firebase Authentication...
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
//                       else if successful...
                        Log.d("Register", "Registered Successfully with id ${it.result?.user?.uid}")

                        uploadData()
                    }
                }
                    .addOnFailureListener {
                        linear_layout_spin_kit_register.visibility = View.GONE

                        Log.d("Register", "${it.message}")
                        Toast.makeText(this, "Failed to Register:  ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun uploadData() {

//        Firstly get downloadImageUri...

        if (selectedPhotoURI == null) return
        val fileName = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("Images/UsersImages/$fileName")
        storageRef.putFile(selectedPhotoURI!!).addOnCompleteListener {
            storageRef.downloadUrl.addOnSuccessListener {
                downloadImageUri = it.toString()

//                Now upload all Data to Firebase...

                val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
                val dbRef = FirebaseDatabase.getInstance().getReference("UsersInfo/$currentUser")
//                val dbRef = FirebaseDatabase.getInstance().getReference("UsersInfo")

                val registerModel = RegisterModel(name, email, password, downloadImageUri.toString())

                dbRef.setValue(registerModel).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                    .addOnFailureListener() {
                        Log.d("Register", "${it.message}")
                        spin_kit_view_register.visibility = View.GONE
                        Toast.makeText(this, "Failed to Upload Data:  ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun showSnackBar() {
        val linearLayout = linear_layout_register
        val snackbar = Snackbar.make(linearLayout, "Please Fill All Fields", Snackbar.LENGTH_SHORT)
            .setActionTextColor(Color.WHITE).setAction("Ok") { }
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"))
        snackbar.show()
    }
}

class User(val uid: String, val userName: String, val profileImageUrl: String) {
    constructor() : this("", "", "")
}
