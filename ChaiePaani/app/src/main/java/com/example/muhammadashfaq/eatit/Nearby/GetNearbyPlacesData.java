package com.example.muhammadashfaq.eatit.Nearby;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];

        DownloadUrl downloadURL = new DownloadUrl();
        try {
            googlePlacesData = downloadURL.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        List<HashMap<String, String>> nearbyPlaceList;
        DataParser parser = new DataParser();
        nearbyPlaceList = parser.parse(s);
        Log.d("nearbyplacesdata","called parse method");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Common.NEARBY_RESTURANTS_LIST = nearbyPlaceList;
            showNearbyPlaces(nearbyPlaceList);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            Log.d("nearbyplacesdata",placeName+"\n"+vicinity);
            double lat = Double.parseDouble(Objects.requireNonNull(googlePlace.get("lat")));
            double lng = Double.parseDouble(Objects.requireNonNull(googlePlace.get("lng")));

            LatLng latLng = new LatLng( lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));


        }
    }
}
