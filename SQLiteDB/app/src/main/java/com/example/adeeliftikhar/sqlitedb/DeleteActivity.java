package com.example.adeeliftikhar.sqlitedb;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.adeeliftikhar.sqlitedb.Database.DatabaseOperations;

public class DeleteActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDB;
    DatabaseOperations databaseOperations;
    EditText deleteStudent;
    String incomingDeleteStudentRollNo;
    Button buttonDeleteStudentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        databaseOperations = new DatabaseOperations(DeleteActivity.this);
        deleteStudent = findViewById(R.id.delete_student);
        buttonDeleteStudentRecord = findViewById(R.id.button_delete_student_record);
        buttonDeleteStudentRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingDeleteStudentRollNo = deleteStudent.getText().toString();
                if (incomingDeleteStudentRollNo.isEmpty()) {
                    deleteStudent.setError("Please Fill This Field");
                } else {
                    sqLiteDB = databaseOperations.getWritableDatabase();
                    databaseOperations.deleteStudent(incomingDeleteStudentRollNo, sqLiteDB);
                    deleteStudent.setText("");
                    Intent intent = new Intent(DeleteActivity.this, StudentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
