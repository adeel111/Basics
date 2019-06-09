package com.example.muhammadashfaq.eatit;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.muhammadashfaq.eatit.Adapter.PlaceListAdapter;
import com.example.muhammadashfaq.eatit.Model.Place;
import com.example.muhammadashfaq.eatit.utils.GoogleApiUrl;

import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {
    public static final String TAG = PlaceListActivity.class.getSimpleName();

    /**
     * ArrayList to store the Near By Place List
     */
    private ArrayList<Place> mNearByPlaceArrayList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private PlaceListAdapter mPlaceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

/**
 * get the intent and get the location Tag
 */

        String currentLocation = getSharedPreferences(
                GoogleApiUrl.CURRENT_LOCATION_SHARED_PREFERENCE_KEY, 0)
                .getString(GoogleApiUrl.CURRENT_LOCATION_DATA_KEY, null);

        String locationQueryStringUrl = GoogleApiUrl.BASE_URL + GoogleApiUrl.NEARBY_SEARCH_TAG + "/" +
                GoogleApiUrl.JSON_FORMAT_TAG + "?" + GoogleApiUrl.LOCATION_TAG + "=" +
                currentLocation + "&" + GoogleApiUrl.RADIUS_TAG + "=" +
                GoogleApiUrl.RADIUS_VALUE + "&" + GoogleApiUrl.PLACE_TYPE_TAG + "=restaurant" +
                "&" + GoogleApiUrl.API_KEY_TAG + "=" + GoogleApiUrl.API_KEY;

        Log.d(TAG, locationQueryStringUrl);

        Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        String actionBarTitleText = getResources().getString(R.string.near_by_tag) +
                " Resturants"+ " " + getString(R.string.list_tag);
        setTitle(actionBarTitleText);
        actionBar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNearByPlaceArrayList = getIntent()
                .getParcelableArrayListExtra(GoogleApiUrl.ALL_NEARBY_LOCATION_KEY);
        mRecyclerView = (RecyclerView) findViewById(R.id.place_list_recycler_view);

        if (mNearByPlaceArrayList.size() == 0) {
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            findViewById(R.id.empty_view).setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mGridLayoutManager = new GridLayoutManager(this, 1);
            mPlaceListAdapter = new PlaceListAdapter(this, mNearByPlaceArrayList);
            mRecyclerView.setLayoutManager(mGridLayoutManager);
            mRecyclerView.setAdapter(mPlaceListAdapter);
        }
    }
}
