package com.example.aydil.simplerecyclerview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.aydil.simplerecyclerview.adapter.RecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    //    It will set the data for Recycler View...
    private RecyclerViewAdapter adapter;
    //    It will manage the Layout of Recycler View (size and positions of components)...
    private RecyclerView.LayoutManager layoutManager;
    String[] countryNames = {"Pakistan", "Japan", "Kazakhstan", "Greece", "Colombia",
            "Canada", "Austria", "France", "Jamaica", "Kenya", "Libya", "Kuwait", "Jordan",
            "Switzerland", "Philippines"
    };
    String[] countryCapitals = {"Islamabad", "Tokyo", "Astana", "Athens", "Bogota", "Ottawa",
            "Vienna", "Paris", "Kingston", "Nairobi", "Tripoli", "Kuwait City", "Amman",
            "Bern", "Manila"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new RecyclerViewAdapter(MainActivity.this, countryNames, countryCapitals);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }
}
