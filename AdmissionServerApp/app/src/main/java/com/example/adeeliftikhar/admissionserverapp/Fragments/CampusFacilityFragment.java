package com.example.adeeliftikhar.admissionserverapp.Fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admissionserverapp.Adapter.FacilityRecyclerAdapter;
import com.example.adeeliftikhar.admissionserverapp.DataProvider.FacilityDataProvider;
import com.example.adeeliftikhar.admissionserverapp.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.admissionserverapp.Model.FacilityModel;
import com.example.adeeliftikhar.admissionserverapp.Model.SuperiorTeamModel;
import com.example.adeeliftikhar.admissionserverapp.R;
import com.example.adeeliftikhar.admissionserverapp.ViewHolder.FacilityViewHolder;
import com.example.adeeliftikhar.admissionserverapp.ViewHolder.TeamViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CampusFacilityFragment extends Fragment {

    ImageView imageViewFacility;
    EditText editTextFacilityName, editTextFacilityDesc;
    Button buttonAddFacility, buttonDismiss;
    int galleryPic = 1;
    String imageURI;
    boolean gotImage;
    ProgressDialog progressDialogSend;

    private RelativeLayout relativeLayoutFacility;
    private SpinKitView spinKitViewFacility;
    private RecyclerView recyclerViewFacility;
    private RelativeLayout relativeLayoutFabButton;
    private FloatingActionButton fabButtonAddFacility;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    FacilityModel facilityModel;

    public CampusFacilityFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_campus_facility, container, false);

        relativeLayoutFacility = view.findViewById(R.id.relative_layout_facility);
        spinKitViewFacility = view.findViewById(R.id.spin_kit_view_facility);
        relativeLayoutFabButton = view.findViewById(R.id.relative_layout_fab_button);

        fabButtonAddFacility = view.findViewById(R.id.fab_button_add_facility);
        fabButtonAddFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialogBoxAdd();
            }
        });

        dbRef = FirebaseDatabase.getInstance().getReference().child("Facilities");
        dbRef.keepSynced(true);
        storageRef = FirebaseStorage.getInstance().getReference().child("Facilities");

        recyclerViewFacility = view.findViewById(R.id.recycler_view_facility);
        recyclerViewFacility.setHasFixedSize(true);
        recyclerViewFacility.setLayoutManager(new LinearLayoutManager(getContext()));

//      Load Data from Fire_base Database...

        relativeLayoutFabButton.setVisibility(View.GONE);
        loadDataFromFirebaseDB();
        return view;
    }

    public void openAlertDialogBoxAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.facility_alert_dialog_design, null);
        imageViewFacility = view.findViewById(R.id.image_view_facility);
        imageViewFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        editTextFacilityName = view.findViewById(R.id.edit_text_facility_name);
        editTextFacilityDesc = view.findViewById(R.id.edit_text_facility_desc);
        buttonAddFacility = view.findViewById(R.id.button_add_facility);
        buttonDismiss = view.findViewById(R.id.button_dismiss_facility);

        buttonAddFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckInternetConnectivity.isConnected(getContext())) {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    uploadData();
                }
            }
        });

        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.show();
        buttonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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
                imageViewFacility.setImageBitmap(bitmapImage);
                gotImage = true;
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

    private void uploadData() {
        Boolean result = validateData();
        if (!result || !gotImage) {
            showCnackBar();
        } else {
            showProgressDialogSend();
//        First get image URI, then call sendAllData() method on its onSuccessListener...
            getImageUri();
        }
    }

    private boolean validateData() {
        String stringName = editTextFacilityName.getText().toString();
        String stringDescription = editTextFacilityDesc.getText().toString();

        return !stringName.isEmpty() && !stringDescription.isEmpty();
    }

    private void getImageUri() {
        Bitmap bitmapImageChairman = ((BitmapDrawable) imageViewFacility.getDrawable()).getBitmap();
        ByteArrayOutputStream baosChairman = new ByteArrayOutputStream();
        bitmapImageChairman.compress(Bitmap.CompressFormat.JPEG, 100, baosChairman);
        final byte[] dataChairman = baosChairman.toByteArray();

//        Thia line will generate random id at which image will store...
        String imageRandomID = UUID.randomUUID().toString();

        StorageReference imageFilepathChairman = storageRef.child("Image ID ==> " + imageRandomID);
//                Now putting image by using this path.
        imageFilepathChairman.putBytes(dataChairman).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    imageURI = task.getResult().getDownloadUrl().toString();
                    sendAllData();
                }
            }
        });
    }

    private void sendAllData() {

        final String stringName = editTextFacilityName.getText().toString();
        String stringDescription = editTextFacilityDesc.getText().toString();

        facilityModel = new FacilityModel(stringName, stringDescription, imageURI);

        dbRef.push().setValue(facilityModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialogSend.dismiss();
                    Toast.makeText(getContext(), "Data Upload Successfully", Toast.LENGTH_SHORT).show();
//                    Now clear all fields...
                    clearAllFields();
                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void clearAllFields() {
        editTextFacilityName.setText("");
        editTextFacilityDesc.setText("");
        imageViewFacility.setImageResource(R.drawable.common_pic_place_holder);
    }

    private void loadDataFromFirebaseDB() {

        FirebaseRecyclerAdapter<FacilityModel, FacilityViewHolder> adapter = new
                FirebaseRecyclerAdapter<FacilityModel, FacilityViewHolder>
                        (FacilityModel.class,
                                R.layout.recycler_view_facility_design,
                                FacilityViewHolder.class,
                                dbRef) {

                    @Override
                    protected void populateViewHolder(FacilityViewHolder viewHolder, FacilityModel model, int position) {

                        viewHolder.setName(model.getName());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setImage(model.getImage());

                        spinKitViewFacility.setVisibility(View.GONE);
                        relativeLayoutFabButton.setVisibility(View.VISIBLE);

//                        Get Id or Key of user on Recycler Clicked Item.
//                        getRef() ==> Will Get DatabaseReference then we will get the current user key or id.

                        final String userKey = getRef(position).getKey();
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "View Clicked Key" + userKey, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
        recyclerViewFacility.setAdapter(adapter);
    }

    private void showProgressDialogSend() {
        progressDialogSend = new ProgressDialog(getContext());
        progressDialogSend.setMessage("Uploading Data...");
        progressDialogSend.setCancelable(false);
        progressDialogSend.show();
    }

    private void showCnackBar() {
        Snackbar mySnackbar = Snackbar.make(relativeLayoutFacility, "Please Fill All Fields and Insert Image", Snackbar.LENGTH_SHORT);
        View snackBarView = mySnackbar.getView();
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"));
        mySnackbar.show();
    }
}
