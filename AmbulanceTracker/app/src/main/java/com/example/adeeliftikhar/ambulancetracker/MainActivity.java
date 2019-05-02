package com.example.adeeliftikhar.ambulancetracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.ambulancetracker.Models.HelperHistoryModel;
import com.example.adeeliftikhar.ambulancetracker.SessionsManager.LoginSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ImageView imageViewAccident;
    FloatingActionButton fabButtonTakePic, fabButtonNotifyTracking;
    Button buttonMakeSimpleCall;
    ProgressBar progressBarMain;
    String currentDateTimeString;

    Animation scaleAnimation;

    FirebaseAuth mAuth;
    String currentUser;
    DatabaseReference dbRefData;
    DatabaseReference dbRefUser;
    StorageReference storageRef;
    String imageURI;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Inform Rescue Team");

        initializerAndClickListeners();

        getCurrentDateAndTime();
    }

    @SuppressLint("RestrictedApi")
    private void initializerAndClickListeners() {

        progressBarMain = findViewById(R.id.progress_bar_main);
        progressBarMain.setVisibility(View.GONE);
        imageViewAccident = findViewById(R.id.image_view_accident);
        fabButtonTakePic = findViewById(R.id.fab_button_take_pic);
        fabButtonTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getImage();
                }
            }
        });
        buttonMakeSimpleCall = findViewById(R.id.button_make_simple_call);
        buttonMakeSimpleCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSimpleCall();
            }
        });
        fabButtonNotifyTracking = findViewById(R.id.fab_button_notify_tracking);
//        Make FabButtonNotifyTracking Un-Visible...
        fabButtonNotifyTracking.setVisibility(View.GONE);
        fabButtonNotifyTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarMain.setVisibility(View.VISIBLE);
                getImageUri();
            }
        });
//        Database Initialization...
        mAuth = FirebaseAuth.getInstance();
        currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        dbRefData = FirebaseDatabase.getInstance().getReference().child("HelperHistory").child(currentUser);
        dbRefUser = FirebaseDatabase.getInstance().getReference().child("PhoneNumberUsers").child(currentUser);
        storageRef = FirebaseStorage.getInstance().getReference().child("IncidentImage").child(currentUser);

    }

    private void getCurrentDateAndTime() {
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getImage() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageViewAccident.setImageBitmap(photo);
            fabButtonNotifyTracking.setVisibility(View.VISIBLE);
            scaleAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.point_arrow_anim);
            fabButtonNotifyTracking.setAnimation(scaleAnimation);
        }
    }

    private void makeSimpleCall() {
        String number = "03066798594";
        if (number.isEmpty()) {
            String numStart = "03066798594";
            Uri call = Uri.parse("tel:" + numStart);
            Intent surf = new Intent(Intent.ACTION_DIAL, call);
            startActivity(surf);
        } else {
            Uri call = Uri.parse("tel:" + number);
            Intent surf = new Intent(Intent.ACTION_DIAL, call);
            startActivity(surf);
        }
    }

    private void getImageUri() {
        Bitmap bitmapImage = ((BitmapDrawable) imageViewAccident.getDrawable()).getBitmap();
        ByteArrayOutputStream baosImage = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baosImage);
        final byte[] dataVictim = baosImage.toByteArray();

        final String imageRandomID = UUID.randomUUID().toString();
        StorageReference imageFilepath = storageRef.child(imageRandomID);

        imageFilepath.putBytes(dataVictim).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageURI = uri.toString();
                        saveDataToDB();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void saveDataToDB() {
        LoginSessionManager loginSessionManager = new LoginSessionManager(MainActivity.this);
        String name = loginSessionManager.getName();
        String number = loginSessionManager.getNumber();
        String date_time = currentDateTimeString;
        String ambulance = "Ambulance 571, Sargodha";
        String current_user_id = currentUser;
        if (name.isEmpty() || number.isEmpty() || date_time.isEmpty() || ambulance.isEmpty() || current_user_id.isEmpty()) {
            Toast.makeText(this, "No Credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        HelperHistoryModel helperHistoryModel = new HelperHistoryModel(name, number, date_time, ambulance, current_user_id, imageURI);
        dbRefData.push().setValue(helperHistoryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBarMain.setVisibility(View.GONE);
                    Intent intent = new Intent(MainActivity.this, TrackingActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Error ==> " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        }
        if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }
        if (id == R.id.delete) {
            deleteAlertBox();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAlertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete Account");
        builder.setMessage("Do you want to Delete your Account?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    deleteAccount();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void deleteAccount() {
        progressBarMain.setVisibility(View.VISIBLE);
        final String phoneNumber = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        final String phoneNumber2 = "12345678901";

        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).unlink(PhoneAuthProvider.PROVIDER_ID).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    assert phoneNumber != null;
                    PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(phoneNumber, phoneNumber2);
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                            deleteAllData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failure ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failure ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllData() {
        dbRefData.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dbRefUser.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                clearLoginSession();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failure Inside ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failure Outside ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearLoginSession() {
        LoginSessionManager loginSessionManager = new LoginSessionManager(MainActivity.this);
        loginSessionManager.loginTheUser(false, "", "");
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
