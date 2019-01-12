package com.example.adeeliftikhar.addnotes;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//      trim() ==> This methods is used no exclude empty spaces before and after the String.

import com.example.adeeliftikhar.addnotes.Adapters.NotesRVAdapter;
import com.example.adeeliftikhar.addnotes.SQLiteDatabase.DatabaseOperations;


public class MainActivity extends AppCompatActivity {

    private static DatabaseOperations databaseOperations;
    private static SQLiteDatabase sqLiteDatabase;
    private static NotesRVAdapter myAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customToolbar();
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
        buildRecyclerView();
    }

    private void customToolbar() {
        Toolbar customToolbar = findViewById(R.id.custom_tool_bar);
        setSupportActionBar(customToolbar);
    }

    private void buildRecyclerView() {
        recyclerView = findViewById(R.id.notes_recycler_view);
        recyclerView.setHasFixedSize(true);
        databaseOperations = new DatabaseOperations(MainActivity.this);
        sqLiteDatabase = databaseOperations.getReadableDatabase();
        myAdapter = new NotesRVAdapter(this, getAllItems());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }


    public void gotoAddNotesActivity(View view) {
        Intent myIntent = new Intent(MainActivity.this, AddNotesActivity.class);
        startActivity(myIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.profile:
                Toast.makeText(this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.general:
                Toast.makeText(this, "General Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.features:
                Toast.makeText(this, "Features Clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.setting:
                Toast.makeText(this, "Setting Clicked", Toast.LENGTH_SHORT).show();
                break;
//            case android.R.id.home:
//                finish();
        }
        return true;
    }

    public static Cursor getAllItems() {
        return databaseOperations.showNotes(sqLiteDatabase);
    }
}