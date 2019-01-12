package com.example.adeeliftikhar.addnotes.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseOperations extends SQLiteOpenHelper {
    private static final String dbName = "AddNotes.db";
    private static int dbVersion = 1;
    private Context context;
    private String CREATE_QUERY = "CREATE TABLE notes(id INTEGER PRIMARY KEY AUTOINCREMENT, title Text, date  Text, notes_content Text, timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP);";

    public DatabaseOperations(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
        Log.w("Constructor ==> ", "Database has been Created.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY);
        Log.w("onCreate ==> ", "Created Notes Table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "notes");
        onCreate(db);
    }

    public void insertNotes(String incomingTitle, String incomingDate, String incomingNotes, SQLiteDatabase sqldb) {
        ContentValues cv = new ContentValues();
        cv.put("title", incomingTitle);
        cv.put("date", incomingDate);
        cv.put("notes_content", incomingNotes);
        sqldb.insert("notes", null, cv);
    }

    public void deleteNote(int comingId, SQLiteDatabase sqLiteDB) {
        sqLiteDB.delete("notes", "id = ?", new String[]{String.valueOf(comingId)});
        Toast.makeText(context, "Note has been Deleted", Toast.LENGTH_SHORT).show();
    }

    public Cursor showNotes(SQLiteDatabase sqldb) {
        String[] column = {"id", "title", "date", "notes_content"};
        return sqldb.query("notes", column, null, null, null, null, "timestamp" + " DESC");
    }

    public Cursor showSpecificNote(int comingId, SQLiteDatabase sqldb) {
        String[] column = {"id", "title", "date", "notes_content"};
        return sqldb.query("notes", column, "id = ?", new String[]{String.valueOf(comingId)}, null, null, "timestamp" + " DESC");
    }

    public boolean updateNote(int incomingId, String incomingNoteTitle, String incomingNoteDate, String incomingNotesContent, SQLiteDatabase sqldb) {
        ContentValues cvStudentUpdate = new ContentValues();
        cvStudentUpdate.put("id", incomingId);
        cvStudentUpdate.put("title", incomingNoteTitle);
        cvStudentUpdate.put("date", incomingNoteDate);
        cvStudentUpdate.put("notes_content", incomingNotesContent);
        sqldb.update("notes", cvStudentUpdate, "id = ?", new String[]{String.valueOf(incomingId)});
        return true;
    }
}
