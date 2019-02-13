package com.example.aydil.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    ListView listViewForGames;
    ArrayAdapter arrayAdapterForGames;
    String [] arrayForGames = {"Cricket", "Football", "Hockey",
            "Volleyball", "Tennis", "Golf", "Surfing", "Cycling",
            "Boxing", "Swimming", "Badminton", "Frisbee"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewForGames = (ListView) findViewById(R.id.list_view_for_games);
        arrayAdapterForGames = new ArrayAdapter(MainActivity.this, R.layout.layout_activity_for_adapter, arrayForGames);
        listViewForGames.setAdapter(arrayAdapterForGames);

        listViewForGames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//              Getting text view from view...
                TextView TextView = (TextView) view;
                String gameName = TextView.getText().toString();
                Toast.makeText(MainActivity.this, gameName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
