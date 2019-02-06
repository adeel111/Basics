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
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.addnotes.Adapters.NotesRVAdapter;
import com.example.adeeliftikhar.addnotes.SQLiteDatabase.DatabaseOperations;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EditNoteActivity extends AppCompatActivity {
    EditText editTextEditTitle, editTextEditDate, editTextEditNote;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private SQLiteDatabase sqLiteDB;
    private DatabaseOperations databaseOperations;
    NotesRVAdapter notesRVAdapter;
    CoordinatorLayout coordinatorLayout;
    int comingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        initializer();
//        Back Button
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getIntentMethod();
    }

    @SuppressLint("NewApi")
    private void initializer() {
        databaseOperations = new DatabaseOperations(EditNoteActivity.this);
        editTextEditTitle = findViewById(R.id.edit_text_edit_title);
        editTextEditDate = findViewById(R.id.edit_text_edit_date);
        editTextEditNote = findViewById(R.id.edit_text_edit_note);
        coordinatorLayout = findViewById(R.id.coordinate_layout);

        myCalendar = Calendar.getInstance();
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("NewApi")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        editTextEditDate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditNoteActivity.this, dateSetListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        databaseOperations = new DatabaseOperations(EditNoteActivity.this);
        sqLiteDB = databaseOperations.getWritableDatabase();
    }

    @SuppressLint("NewApi")
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editTextEditDate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void getIntentMethod() {
        Intent myIntent = getIntent();
        comingId = Integer.parseInt(myIntent.getStringExtra("id"));
        String title = myIntent.getStringExtra("title");
        String date = myIntent.getStringExtra("date");
        String noteContent = myIntent.getStringExtra("noteContent");

        editTextEditTitle.setText(title);
        editTextEditDate.setText(date);
        editTextEditNote.setText(noteContent);
    }

    public void editNote(View view) {
        enterSpecificNoteToDb(view);
    }

    public void enterSpecificNoteToDb(View view) {
        String comingTitle = editTextEditTitle.getText().toString();
        String comingDate = editTextEditDate.getText().toString();
        String comingNotes = editTextEditNote.getText().toString();
        if (comingTitle.isEmpty() || comingDate.isEmpty() || comingNotes.isEmpty()) {
            showSnackbarFillFields();
        } else {
            sqLiteDB = databaseOperations.getWritableDatabase();
            databaseOperations.updateNote(comingId, comingTitle, comingDate, comingNotes, sqLiteDB);
            Cursor myCursor = MainActivity.getAllItems();
            notesRVAdapter = new NotesRVAdapter(EditNoteActivity.this, myCursor);
            notesRVAdapter.swapCursor(myCursor);

            editTextEditTitle.setText("");
            editTextEditDate.setText("");
            editTextEditNote.setText("");

            Intent intent = new Intent(EditNoteActivity.this, MainActivity.class);
            startActivity(intent);
            Toast.makeText(EditNoteActivity.this, "Note is Updated", Toast.LENGTH_SHORT).show();
        }
    }

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
