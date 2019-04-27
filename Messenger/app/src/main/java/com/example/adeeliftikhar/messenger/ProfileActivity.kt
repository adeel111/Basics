package com.example.adeeliftikhar.messenger

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.adeeliftikhar.messenger.Internet.CheckInternetConnectivity
import com.example.adeeliftikhar.messenger.Models.RegisterModel
import com.example.adeeliftikhar.messenger.SessionsManager.LoginSessionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import de.hdodenhof.circleimageview.CircleImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.profile_alert_dialog.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    var currentUser: FirebaseUser? = null
    var currentUserId = ""
    var dbRef: DatabaseReference? = null
    var storageRef: StorageReference? = null
    private var downloadImageUri: String? = null
    private val galleryPic = 1
    var gotImage: Boolean = false
    var count: Int = 0

    var id = ""
    var name = ""
    var email = ""
    var password = ""
    var image = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.title = "Profile"
        checkUser()
        retrieveUserProfileData()

        button_update_user_profile.setOnClickListener {
            showUpdateAlertDialog(name, email, image)
        }

        button_delete_user_profile.setOnClickListener {
            deleteAlertBox()
        }
    }

    companion object {
        var imageViewUser: CircleImageView? = null
    }

    private fun checkUser() {
        val mAuth = FirebaseAuth.getInstance()
//        Getting User From Firebase....
        currentUser = mAuth.getCurrentUser()
//        Getting User ID from current user....
        if (currentUser != null) {
            currentUserId = currentUser!!.getUid()
        }
        dbRef = FirebaseDatabase.getInstance().reference.child("UsersInfo").child(currentUserId)
        storageRef =
                FirebaseStorage.getInstance().getReference().child("Images").child("UsersImages").child(currentUserId)
        dbRef?.keepSynced(true)
    }

    private fun retrieveUserProfileData() {
        dbRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (count != 1) {
                    id = dataSnapshot.child("id").value!!.toString()
                    name = dataSnapshot.child("name").value!!.toString()
                    email = dataSnapshot.child("email").value!!.toString()
                    password = dataSnapshot.child("password").value!!.toString()
                    image = dataSnapshot.child("image").value!!.toString()
                    text_view_user_profile_name.setText(name)
                    text_view_user_profile_email.setText(email)
                    if (image != "Default") {
                        Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder)
                            .into(image_view_profile, object : Callback {
                                override fun onSuccess() {
//                            Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                }

                                override fun onError(e: Exception) {
                                    Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder)
                                        .into(image_view_profile)
                                }
                            })
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun changePhoto() {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this@ProfileActivity)
    }

    private fun showUpdateAlertDialog(name: String, email: String, image: String) {
        val builder = AlertDialog.Builder(this@ProfileActivity)
        val view = LayoutInflater.from(this@ProfileActivity).inflate(R.layout.profile_alert_dialog, null)

        imageViewUser = view.image_view_profile_alert
        val imageUri = image
        if (image != "image") {
            Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder)
                .into(imageViewUser, object : Callback {
                    override fun onSuccess() {
                    }

                    override fun onError(e: Exception) {
                        Picasso.get().load(image).placeholder(R.drawable.common_pic_place_holder)
                            .into(imageViewUser)
                    }
                })
        }

        imageViewUser?.setOnClickListener { changePhoto() }

        view.edit_text_user_profile_name_alert.setText(name)
        view.edit_text_user_profile_email_alert.setText(email)

        view.button_update_user_profile_alert.setOnClickListener {
            updateUserEmailAndProfile(view, imageUri)
        }

        builder.setView(view)
        builder.setCancelable(false)

        val alertDialog = builder.show()
        view.button_dismiss.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == galleryPic && resultCode == Activity.RESULT_OK) {
            val image = data!!.data
            CropImage.activity(image).setAspectRatio(1, 1).start(this@ProfileActivity)
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                val resultUri = result.getUri()
                val fileImage = File(resultUri.getPath())
                val compressImage = compressImage(fileImage)

                val bitmapImage = BitmapFactory.decodeByteArray(compressImage, 0, compressImage.size)
                imageViewUser?.setImageBitmap(bitmapImage)
                gotImage = true

                if (resultUri == null) return
//                val fileName = UUID.randomUUID().toString()
                storageRef?.putFile(resultUri)!!.addOnSuccessListener {
                    storageRef!!.downloadUrl.addOnSuccessListener {
                        downloadImageUri = it.toString()
                    }
                }
            }
        }
    }

    private fun compressImage(imageFile: File): ByteArray {
        val imageBitmap: Bitmap
        var thumbByte = ByteArray(0)
        try {
            imageBitmap = Compressor(this@ProfileActivity).setMaxWidth(200)
                .setMaxHeight(200)
                .setQuality(75)
                .compressToBitmap(imageFile)
            val baos = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            thumbByte = baos.toByteArray()
            return thumbByte

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return thumbByte
    }

    private fun updateUserEmailAndProfile(view: View, imageUri: String) {
        count = 1

        val currentUser = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val id = currentUser
        val dbRef = FirebaseDatabase.getInstance().getReference("UsersInfo/$currentUser")

        if (downloadImageUri.isNullOrEmpty()) {
            downloadImageUri = imageUri
            gotImage = true
        }

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || !gotImage) {
            showSnackBar()
        } else {
//            Checking Internet Connectivity...
            if (!CheckInternetConnectivity.isConnected(this)) {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "Updating Profile, Plz wait...", Toast.LENGTH_SHORT).show()
                name = view.edit_text_user_profile_name_alert.text.toString()
                email = view.edit_text_user_profile_email_alert.text.toString()

                val user = FirebaseAuth.getInstance().currentUser ?: return
//        Getting values from session manager...
                val credential = EmailAuthProvider.getCredential(
                    LoginSessionManager(this).getUserEmail(),
                    LoginSessionManager(this).getUserPassword()
                )
                user.reauthenticate(credential).addOnCompleteListener {
                    Log.d("Update", "User re-authenticated.")
                    user.updateEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Update", "User email address updated.")

//                        Update Profile Now...
                            val registerModel = RegisterModel(id, name, email, password, downloadImageUri.toString())

                            dbRef.setValue(registerModel).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()

//                        Update Login Session
                                    val loginSessionManager = LoginSessionManager(this@ProfileActivity)
                                    loginSessionManager.loginTheUser(true, email, password)

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                                .addOnFailureListener() {
                                    Log.d("Updated", "${it.message}")
                                    spin_kit_view_register.visibility = View.GONE
                                    Toast.makeText(
                                        this,
                                        "Failed to Update Data:  ${it.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        }
                    }
                }
            }
        }
    }

    private fun showSnackBar() {
        val linearLayout = linear_layout_profile
        val snackbar =
            Snackbar.make(linearLayout, "Please Fill All Fields \n And Insert an Image", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE).setAction("Ok") { }
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"))
        snackbar.show()
    }

    private fun deleteAlertBox() {
        val builder = android.support.v7.app.AlertDialog.Builder(this@ProfileActivity)
        builder.setTitle("Delete")
        builder.setMessage("Do you want to Delete your Account?")
        builder.setPositiveButton("Delete") { dialog, which ->
            if (!CheckInternetConnectivity.isConnected(this@ProfileActivity)) {
                Toast.makeText(this@ProfileActivity, "No Internet Connection", Toast.LENGTH_SHORT).show()
            } else {
                deleteProgressDialog()
                deleteAccount()
            }
        }.setNegativeButton("Cancel") { dialog, which -> }
        builder.show()
    }

    private fun deleteProgressDialog() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Deleting Your Account.")
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    private fun deleteAccount() {
        count = 1
        deleteFromFirebase()
    }

    private fun deleteFromFirebase() {
        val user = FirebaseAuth.getInstance().currentUser ?: return

//        Getting values from session manager...
        val delEmail = LoginSessionManager(this).getUserEmail()
        val delPassword = LoginSessionManager(this).getUserPassword()

        val credential = EmailAuthProvider.getCredential(delEmail, delPassword)

        user.reauthenticate(credential).addOnCompleteListener {
            user.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef!!.delete().addOnCompleteListener {
                        if (task.isSuccessful) {
                            dbRef?.removeValue()?.addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this, "Your Profile is Deleted", Toast.LENGTH_SHORT).show()
                                    clearLoginSession()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clearLoginSession() {
        val loginSessionManager = LoginSessionManager(this@ProfileActivity)
        loginSessionManager.loginTheUser(false, "", "")
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}
