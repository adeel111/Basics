package com.example.adeeliftikhar.sqlitedb;

import android.content.Intent;
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

public class UpdateStudentActivity extends AppCompatActivity {
    SQLiteDatabase sqldb;
    DatabaseOperations dbOperations;
    EditText updateRollNo, updateName, updateClass, updateMarks, updateAddress;
    String incomingUpdateRollNo, incomingUpdateName, incomingUpdateClass, incomingUpdateMarks, incomingUpdateAddress;
    Button buttonUpdateStudentRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);
        dbOperations = new DatabaseOperations(UpdateStudentActivity.this);
        updateRollNo = findViewById(R.id.update_roll_no);
        updateName = findViewById(R.id.update_name);
        updateClass = findViewById(R.id.update_class);
        updateMarks = findViewById(R.id.update_marks);
        updateAddress = findViewById(R.id.update_address);
        buttonUpdateStudentRecord = findViewById(R.id.button_update_student_record);
//        buttonShowStudentList = findViewById(R.id.button_show_student_list);

        buttonUpdateStudentRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incomingUpdateRollNo = updateRollNo.getText().toString();
                incomingUpdateName = updateName.getText().toString();
                incomingUpdateClass = updateClass.getText().toString();
                incomingUpdateMarks = updateMarks.getText().toString();
                incomingUpdateAddress = updateAddress.getText().toString();
                if (incomingUpdateRollNo.isEmpty() || incomingUpdateClass.isEmpty() || incomingUpdateClass.isEmpty() ||
                        incomingUpdateMarks.isEmpty() || incomingUpdateAddress.isEmpty()) {
                    Toast.makeText(UpdateStudentActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT).show();
//                Now each field will be check individually...
                    if (incomingUpdateRollNo.isEmpty()) {
                        updateRollNo.setError("Please Fill This Field");
                    }
                    if (incomingUpdateName.isEmpty()) {
                        updateName.setError("Please Fill This Field");
                    }
                    if (incomingUpdateClass.isEmpty()) {
                        updateClass.setError("Please Fill This Field");
                    }

                    if (incomingUpdateMarks.isEmpty()) {
                        updateMarks.setError("Please Fill This Field");
                    }
                    if (incomingUpdateAddress.isEmpty()) {
                        updateAddress.setError("Please Fill This Field");
                    }
                }
//                    Now we are going to enter values in Database because form is now Completely Filled.
                else {
                    sqldb = dbOperations.getWritableDatabase();
                    dbOperations.updateData(incomingUpdateRollNo, incomingUpdateName, incomingUpdateClass, incomingUpdateMarks, incomingUpdateAddress, sqldb);
                    Toast.makeText(UpdateStudentActivity.this, "Student Record Updated", Toast.LENGTH_SHORT).show();
                    updateRollNo.setText("");
                    updateName.setText("");
                    updateClass.setText("");
                    updateMarks.setText("");
                    updateAddress.setText("");
                    Intent intent = new Intent(UpdateStudentActivity.this, StudentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
