package com.example.aydil.expandablelistview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.example.aydil.expandablelistview.Adapter.ExpandableListViewAdapter;

public class MainActivity extends AppCompatActivity {
    ExpandableListView expandableListView;
    String[] parentViewArray = {"Computer", "Cars", "Games", "Food", "Countries"};
    String[][] childViewArray = {{"HP", "Dell", "Apple"}, {"BMW", "Honda Civic", "Mercedes"},
            {"Cricket", "Boxing", "Hockey", "Football"}, {"Mango", "Banana", "Orange", "Pizza"},
            {"Pakistan", "China", "Dubai", "England"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        expandableListView = findViewById(R.id.expandable_list_view);
        ExpandableListViewAdapter expandableListViewAdapter;
        expandableListViewAdapter = new ExpandableListViewAdapter(MainActivity.this, parentViewArray, childViewArray);
        expandableListView.setAdapter(expandableListViewAdapter);
    }
}
