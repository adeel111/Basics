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

import com.example.adeeliftikhar.admissionserverapp.Adapter.TeamRecyclerAdapter;
import com.example.adeeliftikhar.admissionserverapp.DataProvider.TeamDataProvider;
import com.example.adeeliftikhar.admissionserverapp.Model.SuperiorTeamModel;
import com.example.adeeliftikhar.admissionserverapp.R;
import com.example.adeeliftikhar.admissionserverapp.ViewHolder.TeamViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperiorTeamFragment extends Fragment {

    FloatingActionButton fabButtonAdd;
    ImageView imageViewTeamMember;
    EditText editTextName, editTextDesignation, editTextMessage;
    Button buttonAddMember;
    int galleryPic = 1;
    String imageURI;
//    Uri resultUri;
    boolean gotImage;
    RelativeLayout relativeLayout;

    private DatabaseReference dbRef;
    private StorageReference storageRef;

    ProgressDialog progressDialogLoad;
    ProgressDialog progressDialogSend;

    RecyclerView recyclerViewTeam;

    SuperiorTeamModel teamModel;

    public SuperiorTeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_superior_team, container, false);


        dbRef = FirebaseDatabase.getInstance().getReference().child("SuperiorTeam");
        dbRef.keepSynced(true);
        storageRef = FirebaseStorage.getInstance().getReference().child("TeamMember");
        relativeLayout = view.findViewById(R.id.relativelayout);

        fabButtonAdd = view.findViewById(R.id.fab_button_add);

        fabButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAlertDialogBox();
            }
        });

        recyclerViewTeam = view.findViewById(R.id.recycler_view_team);
        recyclerViewTeam.setHasFixedSize(true);
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(getContext()));

//      Load Data from Firebase Database...
        showProgressLoadData();
        loadDataFromFirebaseDB();

        return view;
    }

    public void openAlertDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.team_alert_dialog_design, null);
        imageViewTeamMember = view.findViewById(R.id.team_mate_image);
        imageViewTeamMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });

        editTextName = view.findViewById(R.id.team_mate_name);
        editTextDesignation = view.findViewById(R.id.team_mate_designation);
        editTextMessage = view.findViewById(R.id.team_mate_message);
        buttonAddMember = view.findViewById(R.id.button_add_member);

        buttonAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        builder.setView(view);
        builder.show();
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
                imageViewTeamMember.setImageBitmap(bitmapImage);
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
        String stringName = editTextName.getText().toString();
        String stringDesignation = editTextDesignation.getText().toString();
        String stringMessage = editTextMessage.getText().toString();

        return !stringName.isEmpty() && !stringDesignation.isEmpty() && !stringMessage.isEmpty();
    }

    private void getImageUri() {
        Bitmap bitmapImageChairman = ((BitmapDrawable) imageViewTeamMember.getDrawable()).getBitmap();
        ByteArrayOutputStream baosChairman = new ByteArrayOutputStream();
        bitmapImageChairman.compress(Bitmap.CompressFormat.JPEG, 100, baosChairman);
        final byte[] dataChairman = baosChairman.toByteArray();

        String imageRandomID = UUID.randomUUID().toString();
        StorageReference imageFilepathChairman = storageRef.child("Image ID ==> " + imageRandomID);
//        imageFilepathChairman.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//                if (task.isSuccessful()) {
//                    imageURI = task.getResult().getDownloadUrl().toString();
//                    sendAllData();
//                }
//            }
//        });
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

        final String stringName = editTextName.getText().toString();
        String stringDesignation = editTextDesignation.getText().toString();
        String stringMessage = editTextMessage.getText().toString();

//        First way ==> We can enter data in Firebase through Model as below...
        teamModel = new SuperiorTeamModel(stringName, stringDesignation, stringMessage,imageURI);

//        Second way ==> We can enter data in Firebase through HashMap...

//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("name", stringName);
//        hashMap.put("designation", stringDesignation);
//        hashMap.put("message", stringMessage);
//        hashMap.put("image", imageURI);

        dbRef.push().setValue(teamModel).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        editTextName.setText("");
        editTextDesignation.setText("");
        editTextMessage.setText("");
        imageViewTeamMember.setImageResource(R.drawable.common_pic_place_holder);
    }


    private void loadDataFromFirebaseDB() {
//        For this you have to create two other classes, One is viewHolder (to display data) and second
//        model class ( refers to name of nodes from where you are fetching data ).

        FirebaseRecyclerAdapter<SuperiorTeamModel, TeamViewHolder> adapter = new
                FirebaseRecyclerAdapter<SuperiorTeamModel, TeamViewHolder>
                        (SuperiorTeamModel.class,
                                R.layout.recycler_view_team_design,
                                TeamViewHolder.class,
                                dbRef) {

                    @Override
                    protected void populateViewHolder(TeamViewHolder viewHolder, SuperiorTeamModel model, int position) {
                        viewHolder.setName(model.getName());
                        viewHolder.setDesignation(model.getDesignation());
                        viewHolder.setMessage(model.getMessage());
                        viewHolder.setImage(model.getImage());

                        progressDialogLoad.dismiss();

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
        recyclerViewTeam.setAdapter(adapter);
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
        Snackbar mySnackbar = Snackbar.make(relativeLayout, "Please Fill All Fields", Snackbar.LENGTH_SHORT);
        View snackBarView = mySnackbar.getView();
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"));
        mySnackbar.show();
    }
}
