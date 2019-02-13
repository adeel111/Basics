package com.example.adeeliftikhar.ticktalk;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    Button buttonChangeProfilePhoto, buttonUpdateProfileStatus;
    TextView textViewUserName, textViewUserStatus;
    ImageView imageViewProfile;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String currentUserId;
    DatabaseReference dbReference;
    FirebaseApp firebaseApp;
    StorageReference storageRef;
    int galleryPic = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imageViewProfile = findViewById(R.id.image_view_profile);
        textViewUserName = findViewById(R.id.txt_view_user_name);
        textViewUserStatus = findViewById(R.id.txt_view_user_status);
        buttonChangeProfilePhoto = findViewById(R.id.button_change_profile_photo);
        buttonUpdateProfileStatus = findViewById(R.id.button_update_profile_status);
//        Initializing the Database...
        firebaseApp = FirebaseApp.initializeApp(this);
//        Getting Instance(Object) of Firebase Application (e.g ==> TickTalk)...
        mAuth = FirebaseAuth.getInstance();
//        Getting User From Firebase....
        currentUser = mAuth.getCurrentUser();
//        Getting User ID from current user....
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        }
//        Getting Current User...
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
//        App will be run offline...
        dbReference.keepSynced(true);
        RetrieveDataOfTheUser();
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    private void RetrieveDataOfTheUser() {
//        Used to Retrieve Value from Database...
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                textViewUserName.setText(name);
                textViewUserStatus.setText(status);
                if (!image.equals("Default")) {
                    Picasso.get().load(image).placeholder(R.drawable.bg).into(imageViewProfile, new Callback() {
                        //                       Callback ==> Response
                        @Override
                        public void onSuccess() {
//                            Toast.makeText(ProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get().load(image).placeholder(R.drawable.ic_launcher_foreground).into(imageViewProfile);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void changePhoto(View view) {
//        CropImage ==> It is a library which is used to get image from external apps (e.g gallery or camera etc)
//        by executing intent automatically (e.g startActivityForResult).
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == galleryPic && resultCode == RESULT_OK) {
//            Now coming picture is stored in image.
            Uri image = data.getData();
//            Will crop the image.
            CropImage.activity(image).setAspectRatio(1, 1).start(ProfileActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Now picture is stored in result.
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
                progressDialog.setTitle("OK");
                progressDialog.setMessage("Image Uploading... ");
                progressDialog.setCancelable(false);
                progressDialog.show();
//                resultUri has the current Image which is not compress yet(will store in image_filepath).
                Uri resultUri = result.getUri();
//                Now Picture is in fileImage and sending to the compression method (compressImage).
                File fileImage = new File(resultUri.getPath());
//                byteImageCompress has the Compressed image (will store in thumb_filepath).
                final byte[] byteImageCompress = compressImage(fileImage);
//                Storage path of image and thumb image.
                StorageReference image_filepath = storageRef.child("profile_images").child(currentUserId + ".jpg");
                final StorageReference thumb_filepath = storageRef.child("profile_images").child("thumbs").child(currentUserId + ".jpg");
//                Now putting image by using that path.
                image_filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_filepath.putBytes(byteImageCompress);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    String thumbNailDownloadLink = task.getResult().getDownloadUrl().toString();
                                    if (task.isSuccessful()) {
                                        Map updateHashMap = new HashMap();
                                        updateHashMap.put("image", imageDownloadLink);
                                        updateHashMap.put("thumb_image", thumbNailDownloadLink);
                                        dbReference.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                progressDialog.dismiss();
//                                                Toast.makeText(ProfileActivity.this, "Successfully Updated the Profile", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private byte[] compressImage(File imageFile) {
        Bitmap imageBitmap;
        byte[] thumbByte = new byte[0];
        try {
            imageBitmap = new Compressor(this).
                    setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(imageFile);
//      Creating byte Stream
//            Input/output Stream controls the input and output of data.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            Now outputStream data send and retrieve data in the form of byteArray.
            thumbByte = baos.toByteArray();
            return thumbByte;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbByte;
    }

    public void updateStatus(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Update Your Status");
        builder.setMessage("Enter New Status");
        final EditText editText = new EditText(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(layoutParams);
        editText.setHint("Update Status");
        builder.setView(editText);
        builder.setCancelable(false);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String status = editText.getText().toString();
                dbReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(ProfileActivity.this, "Status Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
