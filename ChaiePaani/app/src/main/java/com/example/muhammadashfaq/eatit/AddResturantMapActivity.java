package com.example.muhammadashfaq.eatit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddResturantMapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private MapFragment mMapFragment;

    String latitude,longitude,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resturant_map);


        name = getIntent().getStringExtra("name");
        longitude = getIntent().getStringExtra("longitude");
        latitude = getIntent().getStringExtra("latitude");


        /**
         * Get the reference of the map fragment
         */
        mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng resturant = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        googleMap.addMarker(new MarkerOptions().position(resturant)
                .title(name));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(resturant));
    }
}
