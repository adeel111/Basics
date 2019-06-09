package com.example.muhammadashfaq.eatit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Database.Database;
import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.Model.Category;
import com.example.muhammadashfaq.eatit.Pakage.ListenOrder;
import com.example.muhammadashfaq.eatit.Pakage.ListenResturant;
import com.example.muhammadashfaq.eatit.SessionManager.SessionManager;
import com.example.muhammadashfaq.eatit.ViewHolder.MenuViewHolder;
import com.example.muhammadashfaq.eatit.utils.GoogleApiUrl;
import com.facebook.FacebookSdk;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.paperdb.Paper;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {

    //Firebase refrence
    FirebaseDatabase firebaseDatabase;
    DatabaseReference category;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> recyclerAdapter;

    TextView userName;
    FloatingActionButton floatingActionButton;

    SpinKitView spinKitView;

    //Recycler menu
    RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;

    //Add favorites
    Database localDB;


    public static final String TAG = Home.class.getSimpleName();
    public static final int LOCATION_REQUEST_CODE = 100;
    public static final int LOCATION_PERMISSION_CODE = 101;
    //Splash Screen Timer
//    private static final int SPLASH_SCREEN_TIMER = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mCurrentLocationRequest;
    private String mCurrentLocation = "";
    private SharedPreferences mLocationSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        spinKitView=findViewById(R.id.spin_kit_home);
        NavigationView navigationView = findViewById(R.id.nav_view);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        // Kick off the request to build GoogleApiClient.
        buildGoogleApiClient();

        //Firebase init
        firebaseDatabase = FirebaseDatabase.getInstance();
        category = firebaseDatabase.getReference("Category");
        category.keepSynced(true);

        //Add to favrotes
        localDB=new Database(this);

        //Init paerp
        Paper.init(this);


        //For sharing food on facebook
        printKeyHash();

        //Fab

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });

        //NavigationView

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        //Setting up Name of the user in header

        userName = navigationView.getHeaderView(0).findViewById(R.id.txt_vu_name);
        userName.setText(Common.username);


        //Load menu

        recyclerMenu = findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        recyclerMenu.setLayoutManager(layoutManager);

        //Will get data for recyclerView from Firebase by using Adapter.
        if(Common.isConnectedtoInternet(getBaseContext())) {
            loadMenu();
        }else{
            Toast.makeText(Home.this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent service=new Intent(Home.this, ListenOrder.class);
        startService(service);
        Intent serviceResturant=new Intent(Home.this, ListenResturant.class);
        startService(serviceResturant);

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


    private void printKeyHash() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.example.muhammadashfaq.eatit",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest ds=MessageDigest.getInstance("SHA");
                ds.update(signature.toByteArray());
                Log.d("Keyhash",Base64.encodeToString(ds.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void loadMenu() {

           /* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(true);
            progressDialog.show();*/
           spinKitView.setVisibility(View.VISIBLE);

            recyclerAdapter = new FirebaseRecyclerAdapter<Category,MenuViewHolder>
                    (Category.class,R.layout.menu_layout, MenuViewHolder.class,category.getRef()) {
                @Override
                protected void populateViewHolder(final MenuViewHolder viewHolder, final Category model, final int position) {
                    spinKitView.setVisibility(View.GONE);
                    viewHolder.menuName.setText(model.getName());
                    Picasso.get().load(model.getImage()).placeholder(R.drawable.placeholder)
                            .into(viewHolder.imageView);



                    //Add to favorites
                    //Add to Favorites
                    if(localDB.isFavorite(recyclerAdapter.getRef(position).getKey())){
                        viewHolder.imageViewfavMenu.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }

                    //change status of favorite
                    viewHolder.imageViewfavMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!localDB.isFavorite(recyclerAdapter.getRef(position).getKey())){
                                localDB.addToFavorites(recyclerAdapter.getRef(position).getKey());
                                viewHolder.imageViewfavMenu.setImageResource(R.drawable.ic_favorite_black_24dp);
                                Toast.makeText(Home.this, " "+ model.getName()+" added to Favorties", Toast.LENGTH_SHORT).show();

                            }else{
                                localDB.removeFromFavorites(recyclerAdapter.getRef(position).getKey());
                                viewHolder.imageViewfavMenu.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                Toast.makeText(Home.this, " "+ model.getName()+" removed from Favorties", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    final Category clickitem = model;
                    viewHolder.setItemClickListner(new ItemClickListner() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {

                            //Getting category id
                            Intent menuId = new Intent(Home.this, FoodList.class);
                            //Because we just want key to only item key will be send
                            menuId.putExtra("CategoryId", recyclerAdapter.getRef(position).getKey());
                            menuId.putExtra("CategoryName", model.getName());
                            startActivity(menuId);
                        }
                    });


                }
            };
            recyclerAdapter.notifyDataSetChanged();
            recyclerMenu.setAdapter(recyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.refresh){
            if(Common.isConnectedtoInternet(getBaseContext())) {
                loadMenu();
            }else{
                Toast.makeText(Home.this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
            }

        }else if(item.getItemId()==R.id.logout){
            SessionManager sessionManager=new SessionManager(getBaseContext());
            sessionManager.logTheUserIn(false,"","");
            Intent intent=new Intent(Home.this,MainActivity.class);
            startActivity(intent);
            finish();

        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (id == R.id.nav_menu) {
            toolbar.setTitle("Menu");

        } else if (id == R.id.nav_cart) {
            toolbar.setTitle("Cart");
            Intent intent = new Intent(Home.this, Cart.class);
            startActivity(intent);
        } else if (id == R.id.nav_orders) {
            toolbar.setTitle("Order");
            Intent intent = new Intent(Home.this, Order.class);
            startActivity(intent);
        } else if (id == R.id.nav_nearby_resturants) {
            toolbar.setTitle("NearbyResturants");
            Intent intent = new Intent(Home.this, NearbyResturants.class);
            startActivity(intent);
        } else if (id == R.id.nav_add_resturants) {
            toolbar.setTitle("Add your Resturant");
            Intent intent = new Intent(Home.this, AddResturant.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SessionManager sessionManager = new SessionManager(getBaseContext());
            sessionManager.logTheUserIn(false, "", "");
            Intent intent = new Intent(Home.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_added_res_resturants){
            toolbar.setTitle("Recently added Resturant");
            Intent intent = new Intent(Home.this, RecentlyAddedResActivity.class);
            startActivity(intent);
        }else{

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
    protected void onDestroy() {
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
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    new AlertDialog.Builder(Home.this)
                            .setTitle(R.string.location_permission_title)
                            .setMessage(R.string.location_permission_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ActivityCompat.requestPermissions(Home.this, new String[]{
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

        mLocationSharedPreferences = getApplicationContext().getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0);

        //SharedPreference to store current location
        SharedPreferences.Editor locationEditor = mLocationSharedPreferences.edit();
        locationEditor.putString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, mCurrentLocation);
        locationEditor.apply();
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
                    status.startResolutionForResult(Home.this, LOCATION_REQUEST_CODE);
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

        SharedPreferences.Editor locationEditor = mLocationSharedPreferences.edit();
        locationEditor.putString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, mCurrentLocation);
        locationEditor.apply();

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
        result.setResultCallback(Home.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                getGPSPermission();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

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
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient,
                        mCurrentLocationRequest,
                        this);
            } else {
                new AlertDialog.Builder(Home.this)
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
