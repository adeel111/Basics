package com.example.adeeliftikhar.admissionserverapp.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adeeliftikhar.admissionserverapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
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

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChairmanMessageFragment extends Fragment {
    private ImageView imageViewChairman;
    private EditText chairmanName, chairmanMessage;
    private Button buttonSendData;
    String stringName, stringMessage;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    private int galleryPic = 1;
    ProgressDialog progressDialog;
    ProgressDialog progressDialogSend;

    public ChairmanMessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chairman_message, container, false);

//        Initializing...
        imageViewChairman = view.findViewById(R.id.image_view_chairman);
        chairmanName = view.findViewById(R.id.chairman_name);
        chairmanMessage = view.findViewById(R.id.chairman_message);
        buttonSendData = view.findViewById(R.id.button_send_data);

        dbRef = FirebaseDatabase.getInstance().getReference().child("ChairmanMessage");
        storageRef = FirebaseStorage.getInstance().getReference().child("Chairman");

        getDataFromFirebase();
        showProgressDialog();


        imageViewChairman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
        buttonSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToFirebase();
            }
        });

        return view;
    }

    private void getImage() {
//        CropImage ==> It is a library which is used to get image from external apps (e.g gallery or camera etc)
//        by executing intent automatically (e.g startActivityForResult).
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == galleryPic && resultCode == RESULT_OK) {
//            Now coming picture is stored in image Uri.
            Uri image = data.getData();
//            Will crop the image.
            CropImage.activity(image).setAspectRatio(1, 1).start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Now picture is stored in result.
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                resultUri has the current Image which is not compress yet(will store in image_filepath).
                Uri resultUri = result.getUri();
//                Now Picture is in fileImage and sending to the compression method (compressImage).
                File fileImage = new File(resultUri.getPath());
//                byteImageCompress has the Compressed image (will store in thumb_filepath).
                byte[] byteImageCompress = compressImage(fileImage);
//                Convert and set image to ImageView...
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(byteImageCompress, 0, byteImageCompress.length);
                imageViewChairman.setImageBitmap(bitmapImage);
            }
        }
    }

    private byte[] compressImage(File imageFile) {
        Bitmap imageBitmap;
        byte[] thumbByte = new byte[0];
        try {
            imageBitmap = new Compressor(getContext()).
                    setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(imageFile);
//            Creating byte Stream
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

    //    Sending data to Firebase...
    private void sendDataToFirebase() {
        showProgressDialogSend();
        sendTextData();
        sendImage();
    }

    private void sendTextData() {

        stringName = chairmanName.getText().toString();
        stringMessage = chairmanMessage.getText().toString();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", stringName);
        hashMap.put("message", stringMessage);
        hashMap.put("chairman_image", "false");
        dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendImage() {
        Bitmap bitmapImageChairman = ((BitmapDrawable) imageViewChairman.getDrawable()).getBitmap();
        ByteArrayOutputStream baosChairman = new ByteArrayOutputStream();
        bitmapImageChairman.compress(Bitmap.CompressFormat.JPEG, 100, baosChairman);
        final byte[] dataChairman = baosChairman.toByteArray();

        StorageReference imageFilepathChairman = storageRef.child("Chairman" + ".jpg");
//                Now putting image by using this path.
        imageFilepathChairman.putBytes(dataChairman).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                    if (task.isSuccessful()) {
                        Map updateHashMap = new HashMap();
                        updateHashMap.put("chairman_image", imageDownloadLink);
                        dbRef.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                progressDialogSend.dismiss();
                                Toast.makeText(getContext(), "Data Upload Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    //    Getting data to Firebase...
    private void getDataFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue().toString();
                String message = dataSnapshot.child("message").getValue().toString();
                chairmanName.setText(name);
                chairmanMessage.setText(message);
//                Getting image from Database...
                final String chairmanPic = dataSnapshot.child("chairman_image").getValue().toString();
                Picasso.get().load(chairmanPic).placeholder(R.drawable.common_pic_place_holder).into(imageViewChairman, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Exception e) {
                        progressDialog.dismiss();
                        Picasso.get().load(chairmanPic).placeholder(R.drawable.common_pic_place_holder).into(imageViewChairman);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading previous Data, Plz wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void showProgressDialogSend() {
        progressDialogSend = new ProgressDialog(getContext());
        progressDialogSend.setTitle("Uploading");
        progressDialogSend.setMessage("Uploading Data, Plz wait...");
        progressDialogSend.setCancelable(false);
        progressDialogSend.show();
    }
}
