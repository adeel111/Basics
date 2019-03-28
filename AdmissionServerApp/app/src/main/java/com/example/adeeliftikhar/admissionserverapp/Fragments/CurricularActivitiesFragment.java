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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admissionserverapp.Adapter.ActivitiesListAdapter;
import com.example.adeeliftikhar.admissionserverapp.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.admissionserverapp.Model.ActivityModel;
import com.example.adeeliftikhar.admissionserverapp.Model.FacilityModel;
import com.example.adeeliftikhar.admissionserverapp.R;
import com.example.adeeliftikhar.admissionserverapp.ViewHolder.FacilityViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import java.util.HashMap;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurricularActivitiesFragment extends Fragment {

    FloatingActionButton fabButtonAddActivity;
    ImageView imageViewActivity;
    EditText editTextActivityName, editTextActivityDesc;
    Button buttonAddActivity, buttonDismiss;
    int galleryPic = 1;
    String imageURI;
    boolean gotImage;

    ProgressDialog progressDialogLoad;
    ProgressDialog progressDialogSend;

    RecyclerView recyclerViewActivity;
    RelativeLayout relativeLayout;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    ActivityModel activityModel;

    public CurricularActivitiesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_curricular_activities, container, false);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Activities");
        dbRef.keepSynced(true);
        storageRef = FirebaseStorage.getInstance().getReference().child("Activities");
        relativeLayout = view.findViewById(R.id.relativelayout);

        fabButtonAddActivity = view.findViewById(R.id.fab_button_add_activity);

        fabButtonAddActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialogBoxAdd();
            }
        });

        recyclerViewActivity = view.findViewById(R.id.recycler_view_activity);
        recyclerViewActivity.setHasFixedSize(true);
        recyclerViewActivity.setLayoutManager(new LinearLayoutManager(getContext()));

//      Load Data from Firebase Database...
        showProgressLoadData();
        loadDataFromFirebaseDB();
        return view;
    }

    public void openAlertDialogBoxAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_alert_dialog_design, null);
        imageViewActivity = view.findViewById(R.id.image_view_activity);
        imageViewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        editTextActivityName = view.findViewById(R.id.edit_text_activity_name);
        editTextActivityDesc = view.findViewById(R.id.edit_text_activity_desc);
        buttonAddActivity = view.findViewById(R.id.button_add_activity);
        buttonDismiss = view.findViewById(R.id.button_dismiss_activity);

        buttonAddActivity.setOnClickListener(new View.OnClickListener() {
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
                imageViewActivity.setImageBitmap(bitmapImage);
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
        String stringName = editTextActivityName.getText().toString();
        String stringDescription = editTextActivityDesc.getText().toString();

        return !stringName.isEmpty() && !stringDescription.isEmpty();
    }

    private void getImageUri() {
        Bitmap bitmapImageChairman = ((BitmapDrawable) imageViewActivity.getDrawable()).getBitmap();
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

        final String stringName = editTextActivityName.getText().toString();
        String stringDescription = editTextActivityDesc.getText().toString();

        activityModel = new ActivityModel(stringName, stringDescription, imageURI);

        dbRef.push().setValue(activityModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        editTextActivityName.setText("");
        editTextActivityDesc.setText("");
        imageViewActivity.setImageResource(R.drawable.common_pic_place_holder);
    }

    private void loadDataFromFirebaseDB() {

//        Firebase Adapter Requires Four Things...
//        1. Model
//        2. ViewHolder
//        3. RecyclerView Design File...
//        4. Database Reference (from data is being loading)

        FirebaseRecyclerAdapter<FacilityModel, FacilityViewHolder> adapter = new
                FirebaseRecyclerAdapter<FacilityModel, FacilityViewHolder>
                        (FacilityModel.class,
                                R.layout.recycler_view_facility_design,
                                FacilityViewHolder.class,
                                dbRef) {

                    //         populateViewHolder is used to populate (display) View given to RecyclerView...
                    @Override
                    protected void populateViewHolder(FacilityViewHolder viewHolder, FacilityModel model, int position) {
//                        viewHolder ==> holds view...
//                        model ==> used to set and get data from Firebase Database...
//                        position ==> has the position of each item from Firebase Database...
                        viewHolder.setName(model.getName());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setImage(model.getImage());

                        progressDialogLoad.dismiss();
//                        get the key of each item at which data is store in Firebase Database...
                        final String userKey = getRef(position).getKey();
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "View Clicked Key" + userKey, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                };
        recyclerViewActivity.setAdapter(adapter);
    }

    private void showProgressLoadData() {
        progressDialogLoad = new ProgressDialog(getContext());
        progressDialogLoad.setTitle("Loading");
        progressDialogLoad.setMessage("Loading Data, Plz wait...");
        progressDialogLoad.setCancelable(false);
        progressDialogLoad.show();
    }

    private void showProgressDialogSend() {
        progressDialogSend = new ProgressDialog(getContext());
        progressDialogSend.setTitle("Uploading");
        progressDialogSend.setMessage("Uploading Data, Plz wait...");
        progressDialogSend.setCancelable(false);
        progressDialogSend.show();
    }

    private void showCnackBar() {
        Snackbar mySnackbar = Snackbar.make(relativeLayout, "Please Fill All Fields and Insert Image", Snackbar.LENGTH_SHORT);
        View snackBarView = mySnackbar.getView();
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"));
        mySnackbar.show();
    }
}