package com.techndevs.trackingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.techndevs.trackingapp.Constants.Common;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {

    public static final String TAG = HomeActivity.class.getSimpleName();
    public static final int LOCATION_REQUEST_CODE = 100;
    public static final int LOCATION_PERMISSION_CODE = 101;
    //Splash Screen Timer
    private static final int SPLASH_SCREEN_TIMER = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mCurrentLocationRequest;
    private String mCurrentLocation = "";

    EditText edtTxtEamil,edtTxtPassword;
    String email,password;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    Button btnLogin;
    LocationSettingsResult locationSettingsResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initizalizations();

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loggin in");
        progressDialog.setMessage("Please wait a while");
        progressDialog.setCancelable(false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtTxtEamil.getText().toString().trim();
                password = edtTxtPassword.getText().toString().trim();


                if(Common.isConnectedtoInternet(LoginActivity.this)){
                    if(email.isEmpty() && password.isEmpty()){
                        edtTxtEamil.setError("Please enter Email");
                        edtTxtPassword.setError("Please enter Password");
                        edtTxtEamil.requestFocus();
                    }else{
                        performSignUp();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, "Seems you're not connected to internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void performSignUp() {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,String.valueOf(task.getException()) , Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initizalizations() {
        edtTxtEamil=findViewById(R.id.edt_txt_email);
        edtTxtPassword=findViewById(R.id.edt_txt_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    public void onClickSignup(View view) {
        startActivity(new Intent(LoginActivity.this,SignupActivity.class));
        finish();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "onConnected");
        mCurrentLocationRequest = LocationRequest.create();
        mCurrentLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mCurrentLocationRequest.setInterval(60000);

        /**
         * Check runtime permission for Android M and high level SDK
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.location_permission_title)
                            .setMessage(R.string.location_permission_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{
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
            } else{
                getGPSPermission();
            }

        } else
            getGPSPermission();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
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
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    mCurrentLocationRequest,
                    this);
        }

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
        this.locationSettingsResult = locationSettingsResult;

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog all permission are correct;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(LoginActivity.this, LOCATION_REQUEST_CODE);
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
    private void getGPSPermission() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mCurrentLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );
        result.setResultCallback(LoginActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mGoogleApiClient.isConnecting() || !mGoogleApiClient.isConnected())
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnecting() || mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getGPSPermission();
            }

        }

    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mCurrentLocationRequest,
                        this);
            } else {
                if(locationSettingsResult!=null){
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
                                status.startResolutionForResult(LoginActivity.this, LOCATION_REQUEST_CODE);
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
//                new AlertDialog.Builder(LoginActivity.this)
//                        .setTitle(R.string.gps_permission_title)
//                        .setMessage(R.string.gps_permission_message)
//                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Retry for GPS Permission
//                                getGPSPermission();
//                            }
//                        })
//                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Kill the application
//                                finish();
//                            }
//                        }).show();
            }
        }
    }
}
