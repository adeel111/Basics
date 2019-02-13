package com.example.aydil.nestedrecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aydil.nestedrecyclerview.adapters.MainRVAdapter;
import com.example.aydil.nestedrecyclerview.adapters.VerticalRVAdapter;
import com.example.aydil.nestedrecyclerview.dataproviders.HorizontalRVDataProviders;
import com.example.aydil.nestedrecyclerview.dataproviders.VerticalRVDataProviders;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Object> object = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String[] boldHeading, text2, text3, likes, likesAmount, disLikes, disLikesAmount, neutral, neutralAmount;
    public static String[] textViewCountryName, textViewNext;

    public static int[] imgView = {R.drawable.nine, R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine,
            R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.six, R.drawable.five};
    public static int[] imageViewCountry = {R.drawable.pakistan, R.drawable.iran, R.drawable.england, R.drawable.iraq,
            R.drawable.china, R.drawable.australia, R.drawable.brazil};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boldHeading = getResources().getStringArray(R.array.bold_heading);
        text2 = getResources().getStringArray(R.array.text2);
        text3 = getResources().getStringArray(R.array.text3);
        likes = getResources().getStringArray(R.array.likes);
        likesAmount = getResources().getStringArray(R.array.likes_amount);
        disLikes = getResources().getStringArray(R.array.dis_likes);
        disLikesAmount = getResources().getStringArray(R.array.dis_likes_amount);
        neutral = getResources().getStringArray(R.array.neutral);
        neutralAmount = getResources().getStringArray(R.array.neutral_amount);
        textViewCountryName = getResources().getStringArray(R.array.country_name);
        textViewNext = getResources().getStringArray(R.array.country_next);

        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainRVAdapter(this, getObject());
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<Object> getObject() {
        object.add(getVerticalData().get(0));
        object.add(getHorizontalData().get(0));
        return object;
    }

    public static ArrayList<VerticalRVDataProviders> getVerticalData() {
        ArrayList<VerticalRVDataProviders> verticalRVDataProviders = new ArrayList<>();
        int i = 0;
//        Following is a for-each Loop...
        for (String heading : boldHeading) {
            VerticalRVDataProviders dataProviderVertical = new VerticalRVDataProviders(imgView[i], heading, text2[i], text3[i], likes[i], likesAmount[i],
                    disLikes[i], disLikesAmount[i], neutral[i], neutralAmount[i]);
            verticalRVDataProviders.add(dataProviderVertical);
            i++;
        }
        return verticalRVDataProviders;
    }

    public static ArrayList<HorizontalRVDataProviders> getHorizontalData() {
        ArrayList<HorizontalRVDataProviders> horizontalRVDataProviders = new ArrayList<>();
        int j = 0;
//        Following is a for-each Loop...
        for (String countryName : textViewCountryName) {
            HorizontalRVDataProviders dataProviderHorizontal = new HorizontalRVDataProviders(imageViewCountry[j], countryName, textViewNext[j]);
            horizontalRVDataProviders.add(dataProviderHorizontal);
            j++;
        }
        return horizontalRVDataProviders;
    }
}
