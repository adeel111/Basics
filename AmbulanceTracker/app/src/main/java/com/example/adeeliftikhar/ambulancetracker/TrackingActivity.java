package com.example.adeeliftikhar.ambulancetracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.location.LocationListener;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.Directions.DataParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TrackingActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private MapFragment mMapFragment;
    private static final String TAG = TrackingActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private HashMap<String, Marker> mCurrentLocMarkers = new HashMap<>();
    private GoogleMap mMap;
    String currentUID;
    DatabaseReference dbRef;
    Double lat, lng;
    private String driverID;

//    String userKey, driverName;

    LatLng latLngDest;
    LatLng latLngOrigin;

    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    ArrayList<LatLng> locList;
    private Polyline mLine;
    private String key = "currentLoction";
//    String latitude, longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Tracking Ambulance");

//        Getting lat and long from HomeFragment to show user's current location.

//        latitude = getIntent().getStringExtra("latitude");
//        longitude = getIntent().getStringExtra("longitude");

        driverID = "95UFIWDtiyXSlYDC2zMUCcJb9DU2";

        buildGoogleApiClient();

        locList = new ArrayList<LatLng>();

        currentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("LocationUpdates").child(driverID);
        // Initializing
        MarkerPoints = new ArrayList<>();
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(TrackingActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TrackingActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        Showing user current location.

//        LatLng victimLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
//        googleMap.addMarker(new MarkerOptions().position(victimLocation).title("Victim Location"));

//        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

//        CameraUpdate center = CameraUpdateFactory.newLatLng(victimLocation);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
//
//        googleMap.moveCamera(center);
//        googleMap.animateCamera(zoom);

        // Authenticate with Firebase when the Google map is loaded

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        subscribeToUpdates();
    }

    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Vaule of origin
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        //Vaule of destination
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //set Value to enable sensor
        String sensor = "sensor=false";
        //Mode for find directins
        String mode = "mode=driving";
        //Build Full Param
        String params = str_org + "&" + str_dest + "&" + sensor + "&" + mode + "&" + "key=" + getString(R.string.google_api_key);
        //Output foramt
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params + "";
        return url;
    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d(TAG, data.toString());
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void subscribeToUpdates() {

        Log.i(TAG, "subscribeToUpdates()");
        Log.i(TAG, driverID);

//        dbRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////                getDataFromSnapshot(dataSnapshot);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                getDataFromSnapshot(dataSnapshot);
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                getDataFromSnapshot(dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDataFromSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getDataFromSnapshot(DataSnapshot dataSnapshot) {

        if (dataSnapshot.hasChild("latitude") && dataSnapshot.hasChild("longitude")) {
            String lat = dataSnapshot.child("latitude").getValue().toString();
            String lng = dataSnapshot.child("longitude").getValue().toString();
            latLngDest = new LatLng(Double.valueOf(lat), Double.valueOf(lng));

            String key = dataSnapshot.getKey();

            if (!mMarkers.containsKey(key)) {
                mMarkers.put(key, mMap.addMarker(new MarkerOptions()
                        .title("Driver " + "Ashfaq" + " Location")
                        .position(latLngDest)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
            } else {
                mMarkers.get(key).setPosition(latLngDest);
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : mMarkers.values()) {
                builder.include(marker.getPosition());
            }

//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLngDest);
//            markerOptions.title("Driver Location");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//            mMap.addMarker(markerOptions);

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, TrackingActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());

        if (!mCurrentLocMarkers.containsKey(key)) {
            mCurrentLocMarkers.put(key, mMap.addMarker(new MarkerOptions().
                    title("Current Location").
                    position(latLngOrigin)));
        } else {
            mCurrentLocMarkers.get(key).setPosition(latLngOrigin);
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkers.values()) {
            builder.include(marker.getPosition());
        }

        addPolyine(latLngOrigin, latLngDest);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngOrigin));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, TrackingActivity.this);
        }
    }

    private void addPolyine(LatLng latLngOrigin, LatLng latLngDest) {
        if (latLngOrigin != null && latLngDest != null) {
            String url = getRequestUrl(latLngOrigin, latLngDest);
            FetchUrl fetchUrl = new FetchUrl();
            fetchUrl.execute(url);
//            PolylineOptions lineOption = new PolylineOptions().add(latLngOrigin).add(latLngDest).color(Color.BLUE);
//            mMap.addPolyline(lineOption);
        } else {
            Toast.makeText(this, "Something is null", Toast.LENGTH_SHORT).show();
        }
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
}

