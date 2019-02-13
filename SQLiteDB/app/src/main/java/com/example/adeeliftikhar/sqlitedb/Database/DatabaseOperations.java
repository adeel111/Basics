package com.example.adeeliftikhar.sqlitedb.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.adeeliftikhar.sqlitedb.StudentActivity;
import com.example.adeeliftikhar.sqlitedb.UpdateStudentActivity;

public class DatabaseOperations extends SQLiteOpenHelper {

    private static final String DbName = "Student.db";
    private static int DbVersion = 1;
    private Context context;
    //    myUserDatabase.db is the IMMUTABLE DB(can't be alter) .
//    myUserDatabase.SQLite is the MUTABLE DB(can be alter).
    private String CREATE_QUERY = "CREATE TABLE userRegistration(user_name Text, user_email Text, user_password Text, user_phone_number Text, user_address Text);";
    private String CREATE_QUERY_STUDENT = "CREATE TABLE studentList(student_roll_no INTEGER PRIMARY KEY AUTOINCREMENT, student_name Text, student_class Text, student_marks INTEGER, student_address Text);";
    private String CREATE_QUERY_Image = "CREATE TABLE studentImage(student_camera_pic BLOB, student_gallery_pic BLOB);";

    public DatabaseOperations(Context context) {
        super(context, DbName, null, DbVersion);
        this.context = context;
//    constructor will build the empty db for us
//    if it does not exist already!
        Log.w("Constructor ==> ", "Database has been Created.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//    onCreate is the one who will create the structure of tables
//    in the database.
        db.execSQL(CREATE_QUERY);
        Log.w("onCreate ==> ", "Created User Table");
        db.execSQL(CREATE_QUERY_STUDENT);
        Log.w("onCreate ==> ", "Created Student Table");
        db.execSQL(CREATE_QUERY_Image);
        Log.w("onCreate ==> ", "Created Image Table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertNewUser(String incomingUserRegName, String incomingUserRegEmail, String incomingUserRegPassword,
                              String incomingUserRegPhoneNumber, String incomingUserRegAddress, SQLiteDatabase sqldb) {
//      ContentValues ==> It is used to send values in SQLite database.
        ContentValues cv = new ContentValues();
        cv.put("user_name", incomingUserRegName);
        cv.put("user_email", incomingUserRegEmail);
        cv.put("user_password", incomingUserRegPassword);
        cv.put("user_phone_number", incomingUserRegPhoneNumber);
        cv.put("user_address", incomingUserRegAddress);
        sqldb.insert("userRegistration", null, cv);
    }

    //        my method to get all registered users from database of sqlite...
    public Cursor getUserDataFromDatabase(SQLiteDatabase sqldb) {
//        Cursor is used to get values from database.
//        Projection...
        String[] columns = {"user_name", "user_email", "user_password", "user_phone_number", "user_address"};
        Cursor myCursor = sqldb.query("userRegistration", columns, null, null, null, null, null);
        return myCursor;
    }

    public boolean checkUserSignIn(String incomingSignInEmail, String incomingSignInPassword, SQLiteDatabase sqLiteDatabase) {
        Cursor myResultCursor;
//        String[] incomingDataArray = {incomingSignInEmail, incomingSignInPassword};
        myResultCursor = sqLiteDatabase.rawQuery("SELECT user_email,user_password FROM userRegistration where user_email='" + incomingSignInEmail + "' and user_password='" + incomingSignInPassword + "'", null);
        if (myResultCursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public void addStudent(String incomingStudentRollNo, String incomingStudentName, String incomingStudentClass, String incomingStudentMarks, String incomingStudentAddress, SQLiteDatabase sqldb) {
        ContentValues cvStudent = new ContentValues();
        cvStudent.put("student_roll_no", incomingStudentRollNo);
        cvStudent.put("student_name", incomingStudentName);
        cvStudent.put("student_class", incomingStudentClass);
        cvStudent.put("student_marks", incomingStudentMarks);
        cvStudent.put("student_address", incomingStudentAddress);
        sqldb.insert("studentList", null, cvStudent);
    }

    public Cursor getStudentList(SQLiteDatabase sqldb) {
        String[] columns = {"student_roll_no", "student_name", "student_class", "student_marks", "student_address"};
        Cursor myCursor = sqldb.query("studentList", columns, null, null, null, null, null);
        return myCursor;
    }

    public boolean updateData(String incomingUpdateRollNo, String incomingUpdateName, String incomingUpdateClass, String incomingUpdateMarks, String incomingUpdateAddress, SQLiteDatabase sqldb) {
        ContentValues cvStudentUpdate = new ContentValues();
        cvStudentUpdate.put("student_roll_no", incomingUpdateRollNo);
        cvStudentUpdate.put("student_name", incomingUpdateName);
        cvStudentUpdate.put("student_class", incomingUpdateClass);
        cvStudentUpdate.put("student_marks", incomingUpdateMarks);
        cvStudentUpdate.put("student_address", incomingUpdateAddress);
        sqldb.update("studentList", cvStudentUpdate, "student_roll_no = ?", new String[]{incomingUpdateRollNo});
        return true;
    }

    public Integer deleteStudent(String incomingDeleteStudentRollNo, SQLiteDatabase sqLiteDatabase) {
        return sqLiteDatabase.delete("studentList", "student_roll_no = ?", new String[]{incomingDeleteStudentRollNo});
    }

    public void insertImage(byte[] incomingCameraImage, byte[] incomingGalleryImage, SQLiteDatabase sqLiteDatabase) {
        ContentValues cvImage = new ContentValues();
        cvImage.put("student_camera_pic", incomingCameraImage);
        cvImage.put("student_gallery_pic", incomingGalleryImage);
        sqLiteDatabase.insert("studentImage", null, cvImage);
    }

    public Cursor getStudentImage(SQLiteDatabase sqLiteDatabase) {
        String[] columns1 = {"student_camera_pic", "student_gallery_pic"};
        Cursor myCursor = sqLiteDatabase.query("studentImage", columns1, null, null, null, null, null);
        return myCursor;
    }
}