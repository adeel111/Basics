package com.example.adeeliftikhar.sqlitedb;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adeeliftikhar.sqlitedb.Database.DatabaseOperations;

public class StudentActivity extends AppCompatActivity {
    SQLiteDatabase sqldb;
    DatabaseOperations dbOperations;
    EditText studentRollNo, studentName, studentClass, studentMarks, studentAddress;
    String incomingStudentRollNo, incomingStudentName, incomingStudentClass, incomingStudentMarks, incomingStudentAddress;
    Button buttonAddStudent, buttonShowStudentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbOperations = new DatabaseOperations(StudentActivity.this);

        studentRollNo = findViewById(R.id.student_roll_no);
        studentName = findViewById(R.id.student_name);
        studentClass = findViewById(R.id.student_class);
        studentMarks = findViewById(R.id.student_marks);
        studentAddress = findViewById(R.id.student_address);
        buttonAddStudent = findViewById(R.id.button_add_student);
        buttonShowStudentList = findViewById(R.id.button_show_student_list);

        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingStudentRollNo = studentRollNo.getText().toString();
                incomingStudentName = studentName.getText().toString();
                incomingStudentClass = studentClass.getText().toString();
                incomingStudentMarks = studentMarks.getText().toString();
                incomingStudentAddress = studentAddress.getText().toString();
//                if (incomingStudentName.isEmpty() || incomingStudentClass.isEmpty() || incomingStudentRollNo.isEmpty() ||
//                        incomingStudentMarks.isEmpty() || incomingStudentAddress.isEmpty()) {
//                    Toast.makeText(StudentActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
                if (incomingStudentRollNo.isEmpty()) {
                    studentRollNo.setError("Please Fill This Field");
                }
                if (incomingStudentName.isEmpty()) {
                    studentName.setError("Please Fill This Field");
                }
                if (incomingStudentClass.isEmpty()) {
                    studentClass.setError("Please Fill This Field");
                }

                if (incomingStudentMarks.isEmpty()) {
                    studentMarks.setError("Please Fill This Field");
                }
                if (incomingStudentAddress.isEmpty()) {
                    studentAddress.setError("Please Fill This Field");
                }
//                    Now we are going to enter values in Database because form is now Completely Filled.
                else {
                    sqldb = dbOperations.getWritableDatabase();
                    dbOperations.addStudent(incomingStudentRollNo, incomingStudentName, incomingStudentClass, incomingStudentMarks, incomingStudentAddress, sqldb);
                    Toast.makeText(StudentActivity.this, "Student Added", Toast.LENGTH_SHORT).show();
                    studentRollNo.setText("");
                    studentName.setText("");
                    d studentClass.setText("");
                    studentMarks.setText("");
                    studentAddress.setText("");
                }
            }
        });
        buttonShowStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqldb = dbOperations.getReadableDatabase();
                Cursor cursor = dbOperations.getStudentList(sqldb);
                if (cursor.getCount() == 0) {
                    showMessage("Error", "No Student Exist");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                if (cursor.moveToFirst()) {
                    do {
                        buffer.append("RollNo: " + cursor.getString(0) + "\n");
                        buffer.append("Name:  " + cursor.getString(1) + "\n");
                        buffer.append("Class:   " + cursor.getString(2) + "\n");
                        buffer.append("Marks:  " + cursor.getString(3) + "\n");
                        buffer.append("Address: " + cursor.getString(4) + "\n" + "\n");
                        showMessage("Student List", buffer.toString());
                    } while (cursor.moveToNext());
                }
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
