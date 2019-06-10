package com.example.adeeliftikhar.ambulancetracker;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import com.google.android.gms.location.LocationListener;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.ambulancetracker.Models.HelperHistoryModel;
import com.example.adeeliftikhar.ambulancetracker.SessionsManager.LoginSessionManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {

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

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int LOCATION_REQUEST_CODE = 100;
    public static final int LOCATION_PERMISSION_CODE = 101;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mCurrentLocationRequest;
    private String mCurrentLocation = "";

    String latitude, longitude;
    View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Inform Rescue Team");

        initializerAndClickListeners();

        getCurrentDateAndTime();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();
        return view;
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @SuppressLint("RestrictedApi")
    private void initializerAndClickListeners() {

        progressBarMain = view.findViewById(R.id.progress_bar_main);
        progressBarMain.setVisibility(View.GONE);
        imageViewAccident = view.findViewById(R.id.image_view_accident);
        fabButtonTakePic = view.findViewById(R.id.fab_button_take_pic);
        fabButtonTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getImage();
                }
            }
        });
        buttonMakeSimpleCall = view.findViewById(R.id.button_make_simple_call);
        buttonMakeSimpleCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeSimpleCall();
            }
        });
        fabButtonNotifyTracking = view.findViewById(R.id.fab_button_notify_tracking);
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
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
                Toast.makeText(getContext(), "Camera Permission Granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "Camera Permission Denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getGPSPermission();
        }
    }

    @SuppressLint("RestrictedApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            imageViewAccident.setImageBitmap(photo);
            fabButtonNotifyTracking.setVisibility(View.VISIBLE);
            scaleAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.point_arrow_anim);
            fabButtonNotifyTracking.setAnimation(scaleAnimation);
        }
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mCurrentLocationRequest,
                        this);
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.gps_permission_title)
                        .setMessage(R.string.gps_permission_message)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Retry for GPS Permission
                                getGPSPermission();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Kill the application
                                getActivity().finish();
                            }
                        }).show();
            }
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
        LoginSessionManager loginSessionManager = new LoginSessionManager(getContext());
        String name = loginSessionManager.getName();
        String number = loginSessionManager.getNumber();
        String date_time = currentDateTimeString;
        String ambulance = "Ambulance 571, Sargodha";
        String current_user_id = currentUser;
        if (name.isEmpty() || number.isEmpty() || date_time.isEmpty() || ambulance.isEmpty() || current_user_id.isEmpty()) {
            Toast.makeText(getContext(), "No Credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        HelperHistoryModel helperHistoryModel = new HelperHistoryModel(name, number, date_time, ambulance, current_user_id, imageURI);
        dbRefData.push().setValue(helperHistoryModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBarMain.setVisibility(View.GONE);
//                    Toast.makeText(MainActivity.this, "Saved History Data", Toast.LENGTH_SHORT).show();
//            save this data to DB...
                    saveDataToFirebase(latitude, longitude);
                } else {
                    Toast.makeText(getContext(), "Error ==> " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        /**
         * Send Location Request to Google Location Service after Connecting
         * to GoogleApiClient
         */

        Log.d(TAG, "onConnected");
        mCurrentLocationRequest = LocationRequest.create();
        mCurrentLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mCurrentLocationRequest.setInterval(60000);

        /**
         * Check runtime permission for Android M and high level SDK
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.location_permission_title)
                            .setMessage(R.string.location_permission_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                                                    Manifest.permission.ACCESS_FINE_LOCATION},
                                            LOCATION_PERMISSION_CODE);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();
                } else
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_CODE);
            } else
                getGPSPermission();
        } else
            getGPSPermission();

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient) != null) {
            mCurrentLocation = String.valueOf(
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLatitude())
                    + "," + String.valueOf(
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient).getLongitude());
//            Toast.makeText(this, "Location ==> " + mCurrentLocation, Toast.LENGTH_SHORT).show();

            List<String> location = Arrays.asList(mCurrentLocation.split(","));

            latitude = location.get(0);
            longitude = location.get(1);

//            Toast.makeText(this, "latitude ==> " + latitude, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "longitude ==> " + longitude, Toast.LENGTH_SHORT).show();

        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mCurrentLocationRequest,
                    this);
        }
    }

    private void saveDataToFirebase(String comingLatitude, String comingLongitude) {
        final String currentId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Victim").child(currentId);
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("latitude", comingLatitude);
        hashMap.put("longitude", comingLongitude);
        hashMap.put("nearby_rescue", "Rescue 1122");
        hashMap.put("status", "0");
        dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Data Saved to DB", Toast.LENGTH_SHORT).show();
                    saveCurrentUserIDToDB(currentId);
                } else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveCurrentUserIDToDB(String currentUserID) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("CurrentVictim");
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("current_id", currentUserID);
        hashMap.put("victim_image", imageURI);
        dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    Toast.makeText(MainActivity.this, "Current User Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), TrackingActivity.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        Status status = locationSettingsResult.getStatus();

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog all permission are correct;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), LOCATION_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                    e.printStackTrace();
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //get the current location of the user
        mCurrentLocation = String.valueOf(location.getLatitude()) + "," +
                String.valueOf(location.getLongitude());

        Log.d(TAG, "onLocationChange");
    }

    /**
     * GPS Permission Dialog
     */
    private void getGPSPermission() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mCurrentLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
