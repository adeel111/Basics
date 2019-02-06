package com.example.adeeliftikhar.addnotes;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.addnotes.Adapters.NotesRVAdapter;
import com.example.adeeliftikhar.addnotes.DataProvider.NotesDataProvider;
import com.example.adeeliftikhar.addnotes.SQLiteDatabase.DatabaseOperations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AddNotesActivity extends AppCompatActivity {

    private DatabaseOperations databaseOperations;
    private SQLiteDatabase sqLiteDatabase;
    private NotesRVAdapter notesRVAdapter;
    private CoordinatorLayout coordinatorLayout;
    private EditText editTextEnterTitle, editTextEnterDate, editTextEnterNotes;
    String comingTitle, comingDate, comingNotes;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener dateSetListener;


    ArrayList<NotesDataProvider> arrayList = new ArrayList<NotesDataProvider>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        coordinatorLayout = findViewById(R.id.coordinate_layout);
        editTextEnterTitle = findViewById(R.id.edit_text_enter_title);
        editTextEnterDate = findViewById(R.id.edit_text_enter_dat);
        editTextEnterNotes = findViewById(R.id.edit_text_enter_notes);

        myCalendar = Calendar.getInstance();
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        editTextEnterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddNotesActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        databaseOperations = new DatabaseOperations(AddNotesActivity.this);
        sqLiteDatabase = databaseOperations.getWritableDatabase();
    }

//    End of onCreate Method...

    @SuppressLint("NewApi")
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextEnterDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void enterNotesToDb(View view) {
        comingTitle = editTextEnterTitle.getText().toString();
        comingDate = editTextEnterDate.getText().toString();
        comingNotes = editTextEnterNotes.getText().toString();
        if (comingTitle.isEmpty() || comingDate.isEmpty() || comingNotes.isEmpty()) {
            showSnackbarFillFields();
        } else {
            sqLiteDatabase = databaseOperations.getWritableDatabase();
            databaseOperations.insertNotes(comingTitle, comingDate, comingNotes, sqLiteDatabase);
            Toast.makeText(this, "Note is Added", Toast.LENGTH_SHORT).show();
            Cursor myCursor = MainActivity.getAllItems();
            notesRVAdapter = new NotesRVAdapter(AddNotesActivity.this, myCursor);
            notesRVAdapter.swapCursor(myCursor);

            editTextEnterTitle.setText("");
            editTextEnterDate.setText("");
            editTextEnterNotes.setText("");

            sendDataToProvider();

            Intent intent = new Intent(AddNotesActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void sendDataToProvider() {
            NotesDataProvider dataProvider = new NotesDataProvider(comingTitle, comingDate, comingNotes);
            arrayList.add(dataProvider);
    }

//    public void showNotesFromDb(View view) {
//        sqLiteDatabase = databaseOperations.getReadableDatabase();
//        Cursor cursor = databaseOperations.showNotes(sqLiteDatabase);
//        if (cursor == null) {
//            Toast.makeText(this, "Cursor is empty", Toast.LENGTH_SHORT).show();
//        } else {
//            if (cursor.moveToFirst()) {
//                do {
//                    String id = cursor.getString(0);
//                    String title = cursor.getString(1);
//                    String date = cursor.getString(2);
//                    String notes = cursor.getString(3);
//                    Toast.makeText(this, id + ", " + title + ", " + date + ", " + notes, Toast.LENGTH_SHORT).show();
//                } while (cursor.moveToNext());
//            }
//        }
//    }

    private void showSnackbarFillFields() {
        Snackbar mySnackbar = Snackbar.make(coordinatorLayout, "Please Fill All Fields", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        mySnackbar.show();
    }
}
