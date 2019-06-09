package com.app.driverpannel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.driverpannel.Service.TrackerService;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult>  {

    DatabaseReference dbRef;
    String currentVictimID;
    Button btnGoToMaps,btnRescueOffices;
    TextView txtVulatitude,txtVuLongitude;
    ProgressDialog progressDialog;
    public static final String TAG = MapActivity.class.getSimpleName();
    public static final int LOCATION_REQUEST_CODE = 100;
    public static final int LOCATION_PERMISSION_CODE = 101;
    //Splash Screen Timer
    private static final int SPLASH_SCREEN_TIMER = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mCurrentLocationRequest;
    private String mCurrentLocation = "";
    private SharedPreferences mLocationSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);



        btnGoToMaps = findViewById(R.id.btn_gotomaps);
        txtVulatitude = findViewById(R.id.txt_vu_latitude);
        txtVuLongitude = findViewById(R.id.txt_vu_longitude);
        btnRescueOffices = findViewById(R.id.btn_nearby_rescue);
       // btnGoToMaps.setEnabled(false);


        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        dbRef = FirebaseDatabase.getInstance().getReference("CurrentVictim");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    progressDialog.show();
                    currentVictimID = dataSnapshot.child("current_id").getValue().toString();
                    fetchDataOfVictim(currentVictimID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnRescueOffices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,RescueSearchMaps.class));
            }
        });


        btnGoToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this,MapActivity.class);
                startActivity(intent);
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


    private void fetchDataOfVictim(String currentVictimID) {
        DatabaseReference victimRef = FirebaseDatabase.getInstance().getReference("Victim");

        victimRef.child(currentVictimID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    progressDialog.dismiss();
                    final String latitude = dataSnapshot.child("latitude").getValue().toString();
                    final String longitude = dataSnapshot.child("longitude").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();

                    if(latitude!=null && longitude != null && status !=null){

                        txtVulatitude.setText(latitude);
                        txtVuLongitude.setText(longitude);
                    }
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle(R.string.location_permission_title)
                            .setMessage(R.string.location_permission_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{
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
                // getGPSPermission();
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

        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog all permission are correct;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(HomeActivity.this, LOCATION_REQUEST_CODE);
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
        result.setResultCallback(HomeActivity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getGPSPermission();
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
                new AlertDialog.Builder(HomeActivity.this)
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
                                finish();
                            }
                        }).show();
            }
        }
    }
}
