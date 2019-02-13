package com.example.aydil.usedrecyclerveiw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //    android:entries="@array/image_view"   ==> int array in string for images to validate in XML.
    String[] boldHeading, text2, text3, likes, likesAmount, disLikes, disLikesAmount, neutral, neutralAmount;
    int[] imgView = {R.drawable.nine, R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.five, R.drawable.six, R.drawable.seven, R.drawable.eight, R.drawable.nine,
            R.drawable.two, R.drawable.three, R.drawable.four, R.drawable.six, R.drawable.five};

    ArrayList<DataProvider> arrayList = new ArrayList<DataProvider>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        boldHeading = getResources().getStringArray(R.array.bold_heading);
        text2 = getResources().getStringArray(R.array.text2);
        text3 = getResources().getStringArray(R.array.text3);
        likes = getResources().getStringArray(R.array.likes);
        likesAmount = getResources().getStringArray(R.array.likes_amount);
        disLikes = getResources().getStringArray(R.array.dis_likes);
        disLikesAmount = getResources().getStringArray(R.array.dis_likes_amount);
        neutral = getResources().getStringArray(R.array.neutral);
        neutralAmount = getResources().getStringArray(R.array.neutral_amount);
//        imgView = getResources().getIntArray(R.array.image_view);
        int i = 0;
//        Following is a for-each Loop...
        for (String heading : boldHeading) {
            DataProvider dataProvider = new DataProvider(imgView[i], heading, text2[i], text3[i], likes[i], likesAmount[i],
                    disLikes[i], disLikesAmount[i], neutral[i], neutralAmount[i]);
            arrayList.add(dataProvider);
            i++;
        }
        adapter = new RecyclerAdapter(arrayList);
        recyclerView.setHasFixedSize(true);
//              Will show the Recycler View in Linear Layout...
//        layoutManager = new LinearLayoutManager(MainActivity.this);
//              Will show the Recycler View in Grid Layout...
//        layoutManager = new GridLayoutManager(MainActivity.this,3);
//              Will show the Recycler View in both, either Linear or Grid Layout at a time...
        layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
//        layoutManager = new StaggeredGridLayoutManager(2, GridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
