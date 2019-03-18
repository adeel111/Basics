package com.example.adeeliftikhar.admission;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.admission.Internet.CheckInternetConnectivity;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class AdmissionFormActivity extends AppCompatActivity {
    private EditText formNo, date, studyProgram, name, fatherName, fatherOccupation, monthlyIncome, phoneNo, mobile, studentEmail, postalCode, CNICorBayFormNo;
    private String stringFormNo, stringDate, stringStudyProgram, stringName, stringFatherName, stringFatherOccupation, stringMonthlyIncome, stringPhoneNo, stringMobile, stringStudentEmail, stringPostalCode, stringCNICorBayFormNo;

    private ImageView imageViewStudent, imageViewBill, imageViewSalary, imageViewMetricResultCard, imageViewFatherIdCard;
    private Boolean studentImage = false;
    private Boolean studentMetricResultCardImage = false;
    private Boolean studentFatherIDCardImage = false;
    private Boolean studentElectricityBillImage = false;
    private Boolean studentSalarySlipImage = false;
    private String buttonText;

    private int getFormNo;
    ProgressDialog progressDialog;
    ProgressDialog progressDialogSubmit;
    ProgressDialog progressDialogUpdate;

    private Button buttonSubmitForm, buttonEditForm, buttonUpdateForm, buttonDeleteForm;
    private int galleryPic = 1;
    private int a = 1;
    private int b = 1;
    private Spinner spinner;

    private RelativeLayout relativeLayoutForm;
    private SpinKitView spinKitViewForm;
    private LinearLayout linearLayoutForm;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRefAdmissions;
    private DatabaseReference dbRefFormNumber;
    private String currentUser;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_form);
//        Initializing All The Widgets...
        initializer();
//        progressDialog.show();
//        Get Form No From Database...
        getFormNoFromDB();
//        Getting current date...
        currentDate();
//        Calling Intent Method for Setting the related degree which is coming through intent.
        getIntentMethod();
//        Spinner Will make visibility of some items gone and visible on the basis of its item selected.
        spinnerWork();
    }

    private void initializer() {
//        Initializing EditText...
        formNo = findViewById(R.id.form_no);
        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        fatherName = findViewById(R.id.father_name);
        fatherOccupation = findViewById(R.id.father_occupation);
        monthlyIncome = findViewById(R.id.monthly_income);
        phoneNo = findViewById(R.id.phone_no);
        mobile = findViewById(R.id.mobile);
        studentEmail = findViewById(R.id.student_email);
        studyProgram = findViewById(R.id.study_program);
        postalCode = findViewById(R.id.postal_code);
        CNICorBayFormNo = findViewById(R.id.cnic_or_by_form_no);

//        Initializing ImageViews with Click Listeners...
        imageViewStudent = findViewById(R.id.image_view_student);
        imageViewStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonText = "Student Image";
                getImage();
            }
        });

        imageViewMetricResultCard = findViewById(R.id.image_view_metric_result_card);
        imageViewMetricResultCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonText = "Metric Result Card Image";
                getImage();
            }
        });
        imageViewFatherIdCard = findViewById(R.id.image_view_father_id_card);
        imageViewFatherIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonText = "Father ID Card Image";
                getImage();
            }
        });
        imageViewBill = findViewById(R.id.image_view_bill);
        imageViewBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonText = "Bill Image";
                getImage();
            }
        });
        imageViewSalary = findViewById(R.id.image_view_salary);
        imageViewSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonText = "Salary Image";
                getImage();
            }
        });
//        Initializing Buttons...
        buttonSubmitForm = findViewById(R.id.button_submit_form);
        buttonEditForm = findViewById(R.id.button_edit_form);
        buttonUpdateForm = findViewById(R.id.button_update_form);
        buttonDeleteForm = findViewById(R.id.button_delete_form);

//        Initializing Spinner...
        spinner = findViewById(R.id.spinner);

        relativeLayoutForm = findViewById(R.id.relative_layout_form);
        spinKitViewForm = findViewById(R.id.spin_kit_view_form);
        linearLayoutForm = findViewById(R.id.linear_layout_form);

        linearLayoutForm.setVisibility(View.GONE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        dbRefFormNumber = FirebaseDatabase.getInstance().getReference().child("TotalAdmissions");
        storageRef = FirebaseStorage.getInstance().getReference().child("FormImages");
    }

    private void currentDate() {
        String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        date.setText(currentDate);
    }

    private void getIntentMethod() {
        Intent intent = getIntent();
        String comingProgramText = intent.getStringExtra("idProgram");
        studyProgram.setText(comingProgramText);
    }

    private void spinnerWork() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                if (position == 1) {
                    imageViewMetricResultCard.setVisibility(View.VISIBLE);
                    imageViewFatherIdCard.setVisibility(View.VISIBLE);

//                    Make the Needy Students Related data Invisible.

                    imageViewBill.setVisibility(View.GONE);
                    imageViewSalary.setVisibility(View.GONE);
                }
                if (position == 2) {
                    imageViewMetricResultCard.setVisibility(View.VISIBLE);
                    imageViewFatherIdCard.setVisibility(View.VISIBLE);

//                    Make the Needy Students Related data Visible.

                    imageViewBill.setVisibility(View.VISIBLE);
                    imageViewSalary.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getFormNoFromDB() {
        dbRefFormNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild("form_noo")) {
                getFormNo = Integer.parseInt(dataSnapshot.child("total").getValue().toString());
//                Toast.makeText(AdmissionFormActivity.this, "Before ==> " + getFormNo, Toast.LENGTH_SHORT).show();
                int incrementedFormNo = getFormNo + 1;
//                Toast.makeText(AdmissionFormActivity.this, "After ==> " + incrementedFormNo, Toast.LENGTH_SHORT).show();
                String num = String.valueOf(incrementedFormNo);
                formNo.setText(num);
                spinKitViewForm.setVisibility(View.GONE);
                linearLayoutForm.setVisibility(View.VISIBLE);
            }
// else {
//                    formNo.setText("1");
//                    spinKitViewForm.setVisibility(View.GONE);
//                    linearLayoutForm.setVisibility(View.VISIBLE);
//                }
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void buttonSubmitForm(View view) {
        b = 1;
//        First make sure that fields are not empty and correctly filled along with images inserted.
        Boolean response = validateTextData();
        if (!response) {
            showSnackBar();
        } else {
            if (!studentImage) {
                showSnackBar();
            } else {
                String spinnerValue = spinner.getSelectedItem().toString();
                switch (spinnerValue) {
                    case "Choose Scholarship Program":
                        showSnackBar();
                        break;
                    case "Merit Based":
                        if (!studentMetricResultCardImage || !studentFatherIDCardImage) {
                            showSnackBar();
                        } else {
                            if (!CheckInternetConnectivity.isConnected(AdmissionFormActivity.this)) {
//                                Toast.makeText(this, "" + CheckInternetConnectivity.isConnected(AdmissionFormActivity.this), Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            } else {
                                submitTextForm();
                                submitAllImages();
                                showProgressDialogSubmit();
//                            Toast.makeText(this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();

//                            Now clear fields and images..
                                clearTextFields();
                                imageViewStudent.setImageResource(R.drawable.student_place_holder);
                                imageViewMetricResultCard.setImageResource(R.drawable.metric_rc_place_holder);
                                imageViewFatherIdCard.setImageResource(R.drawable.father_id_card_place_holder);

//                            Now Send Data to Database...
//                            Visibility Gone and on.
                                buttonSubmitForm.setVisibility(View.GONE);
                                buttonEditForm.setVisibility(View.VISIBLE);
                                buttonDeleteForm.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case "Needy Based":
                        if (!studentMetricResultCardImage || !studentFatherIDCardImage ||
                                !studentElectricityBillImage || !studentSalarySlipImage) {
                            showSnackBar();
                        } else {
                            if (!CheckInternetConnectivity.isConnected(AdmissionFormActivity.this)) {
                                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            } else {
                                showProgressDialogSubmit();
                                submitTextForm();
                                submitAllImages();
//                            Toast.makeText(this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();

//                            Now clear fields and images..
                                clearTextFields();
                                imageViewStudent.setImageResource(R.drawable.student_place_holder);
                                imageViewMetricResultCard.setImageResource(R.drawable.metric_rc_place_holder);
                                imageViewFatherIdCard.setImageResource(R.drawable.father_id_card_place_holder);
                                imageViewBill.setImageResource(R.drawable.bill_place_holder);
                                imageViewSalary.setImageResource(R.drawable.salary_slip_place_holder);
//                            Now Send Data to Database...
//                            Visibility Gone and on...
                                buttonSubmitForm.setVisibility(View.GONE);
                                buttonEditForm.setVisibility(View.VISIBLE);
                                buttonDeleteForm.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                }
            }
        }
    }

    private boolean validateTextData() {
        stringFormNo = formNo.getText().toString();
        stringDate = date.getText().toString();
        stringStudyProgram = studyProgram.getText().toString();
        stringName = name.getText().toString();
        stringFatherName = fatherName.getText().toString();
        stringFatherOccupation = fatherOccupation.getText().toString();
        stringMonthlyIncome = monthlyIncome.getText().toString();
        stringPhoneNo = phoneNo.getText().toString();
        stringMobile = mobile.getText().toString();
        stringStudentEmail = studentEmail.getText().toString();
        stringPostalCode = postalCode.getText().toString();
        stringCNICorBayFormNo = CNICorBayFormNo.getText().toString();

        return !stringFormNo.isEmpty() && !stringDate.isEmpty() && !stringStudyProgram.isEmpty() && !stringName.isEmpty() && !stringFatherName.isEmpty() &&
                !stringFatherOccupation.isEmpty() && !stringMonthlyIncome.isEmpty() && !stringPhoneNo.isEmpty() && !stringMobile.isEmpty() &&
                !stringStudentEmail.isEmpty() && !stringPostalCode.isEmpty() && !stringCNICorBayFormNo.isEmpty();


    }

    private void submitTextForm() {
//        HashMap to send values...

        HashMap<String, String> hashMapFormNo = new HashMap<>();
        HashMap<String, String> hashMap = new HashMap<>();

        hashMapFormNo.put("total", stringFormNo);

        hashMap.put("form_no", stringFormNo);
        hashMap.put("date", stringDate);
        hashMap.put("study_program", stringStudyProgram);
        hashMap.put("name", stringName);
        hashMap.put("father_name", stringFatherName);
        hashMap.put("father_occupation", stringFatherOccupation);
        hashMap.put("monthly_income", stringMonthlyIncome);
        hashMap.put("phone_no", stringPhoneNo);
        hashMap.put("mobile", stringMobile);
        hashMap.put("email", stringStudentEmail);
        hashMap.put("postal_code", stringPostalCode);
        hashMap.put("student_cnic", stringCNICorBayFormNo);

        hashMap.put("student_image", "false");
        hashMap.put("result_card_image", "false");
        hashMap.put("father_id_card_image", "false");
        hashMap.put("bill_image", "false");
        hashMap.put("salary_slip_image", "false");

        dbRefFormNumber.setValue(hashMapFormNo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("LOG", "Task Successful");
                } else {
                    FirebaseAuthException e = (FirebaseAuthException) task.getException();
                    Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

//        Now switch statement is used to store admission form at Intended Degree Place.

        switch (stringStudyProgram) {
            case "Pre_Medical":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("FSC").child("PreMedical").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "Pre_Engineering":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("FSC").child("PreEngineering").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "Physics":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("ICS").child("Physics").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "States":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("ICS").child("States").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "Banking":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("ICOM").child("Banking").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "Commerce":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("ICOM").child("Commerce").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "IT":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("FA").child("IT").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "Education":
                dbRefAdmissions = FirebaseDatabase.getInstance().getReference().child("Admissions").child("FA").child("Education").child(currentUser);
                dbRefAdmissions.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("LOG", "Task Successful");
                        } else {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(AdmissionFormActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    private void submitAllImages() {
        switch (stringStudyProgram) {
            case "Pre_Medical":
                Bitmap bitmapImageMedicalStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosMedicalStudent = new ByteArrayOutputStream();
                bitmapImageMedicalStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosMedicalStudent);
                final byte[] dataMedicalStudent = baosMedicalStudent.toByteArray();

                StorageReference imageFilepathMedicalStudent = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathMedicalStudent.putBytes(dataMedicalStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageMedicalMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosMedicalMetricResultCard = new ByteArrayOutputStream();
                bitmapImageMedicalMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosMedicalMetricResultCard);
                final byte[] dataMedicalMetricResultCard = baosMedicalMetricResultCard.toByteArray();

                StorageReference imageFilepathMedicalMetricResultCard = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathMedicalMetricResultCard.putBytes(dataMedicalMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageMedicalFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosMedicalFatherIDCard = new ByteArrayOutputStream();
                bitmapImageMedicalFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosMedicalFatherIDCard);
                final byte[] dataMedicalFatherIDCard = baosMedicalFatherIDCard.toByteArray();

                StorageReference imageFilepathMedicalFatherIDCard = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathMedicalFatherIDCard.putBytes(dataMedicalFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageMedicalBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosMedicalBill = new ByteArrayOutputStream();
                bitmapImageMedicalBill.compress(Bitmap.CompressFormat.JPEG, 100, baosMedicalBill);
                final byte[] dataMedicalBill = baosMedicalBill.toByteArray();

                StorageReference imageFilepathMedicalBill = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using that path.
                imageFilepathMedicalBill.putBytes(dataMedicalBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageMedicalSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosMedicalSalary = new ByteArrayOutputStream();
                bitmapImageMedicalSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosMedicalSalary);
                final byte[] dataMedicalSalary = baosMedicalSalary.toByteArray();

                StorageReference imageFilepathMedicalSalary = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathMedicalSalary.putBytes(dataMedicalSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;

            case "Pre_Engineering":
                Bitmap bitmapImageEngineeringStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEngineeringStudent = new ByteArrayOutputStream();
                bitmapImageEngineeringStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosEngineeringStudent);
                final byte[] dataEngineeringStudent = baosEngineeringStudent.toByteArray();

                StorageReference imageFilepathEngineeringStudent = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathEngineeringStudent.putBytes(dataEngineeringStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEngineeringMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEngineeringMetricResultCard = new ByteArrayOutputStream();
                bitmapImageEngineeringMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosEngineeringMetricResultCard);
                final byte[] dataEngineeringMetricResultCard = baosEngineeringMetricResultCard.toByteArray();

                StorageReference imageFilepathEngineeringMetricResultCard = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathEngineeringMetricResultCard.putBytes(dataEngineeringMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEngineeringFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEngineeringFatherIDCard = new ByteArrayOutputStream();
                bitmapImageEngineeringFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosEngineeringFatherIDCard);
                final byte[] dataEngineeringFatherIDCard = baosEngineeringFatherIDCard.toByteArray();

                StorageReference imageFilepathEngineeringFatherIDCard = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathEngineeringFatherIDCard.putBytes(dataEngineeringFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapEngineeringBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEngineeringBill = new ByteArrayOutputStream();
                bitmapEngineeringBill.compress(Bitmap.CompressFormat.JPEG, 100, baosEngineeringBill);
                final byte[] dataEngineeringBill = baosEngineeringBill.toByteArray();

                StorageReference imageFilepathEngineeringBill = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathEngineeringBill.putBytes(dataEngineeringBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEngineeringSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEngineeringSalary = new ByteArrayOutputStream();
                bitmapImageEngineeringSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosEngineeringSalary);
                final byte[] dataEngineeringSalary = baosEngineeringSalary.toByteArray();

                StorageReference imageFilepathEngineeringSalary = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathEngineeringSalary.putBytes(dataEngineeringSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;

            case "Physics":
                Bitmap bitmapImagePhysicsStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosPhysicsStudent = new ByteArrayOutputStream();
                bitmapImagePhysicsStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosPhysicsStudent);
                final byte[] dataPhysicsStudent = baosPhysicsStudent.toByteArray();

                StorageReference imageFilepathPhysicsStudent = storageRef.child("ICS").child("Physics").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathPhysicsStudent.putBytes(dataPhysicsStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImagePhysicsMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosPhysicsMetricResultCard = new ByteArrayOutputStream();
                bitmapImagePhysicsMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosPhysicsMetricResultCard);
                final byte[] dataPhysicsMetricResultCard = baosPhysicsMetricResultCard.toByteArray();

                StorageReference imageFilepathPhysicsMetricResultCard = storageRef.child("ICS").child("Physics").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathPhysicsMetricResultCard.putBytes(dataPhysicsMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImagePhysicsFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosPhysicsFatherIDCard = new ByteArrayOutputStream();
                bitmapImagePhysicsFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosPhysicsFatherIDCard);
                final byte[] dataPhysicsFatherIDCard = baosPhysicsFatherIDCard.toByteArray();

                StorageReference imageFilepathPhysicsFatherIDCard = storageRef.child("ICS").child("Physics").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathPhysicsFatherIDCard.putBytes(dataPhysicsFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImagePhysicsBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosPhysicsBill = new ByteArrayOutputStream();
                bitmapImagePhysicsBill.compress(Bitmap.CompressFormat.JPEG, 100, baosPhysicsBill);
                final byte[] dataPhysicsBill = baosPhysicsBill.toByteArray();

                StorageReference imageFilepathPhysicsBill = storageRef.child("ICS").child("Physics").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathPhysicsBill.putBytes(dataPhysicsBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImagePhysicsSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosPhysicsSalary = new ByteArrayOutputStream();
                bitmapImagePhysicsSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosPhysicsSalary);
                final byte[] dataPhysicsSalary = baosPhysicsSalary.toByteArray();

                StorageReference imageFilepathPhysicsSalary = storageRef.child("ICS").child("Physics").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathPhysicsSalary.putBytes(dataPhysicsSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;

            case "States":
                Bitmap bitmapImageStatesStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosStatesStudent = new ByteArrayOutputStream();
                bitmapImageStatesStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosStatesStudent);
                final byte[] dataStatesStudent = baosStatesStudent.toByteArray();

                StorageReference imageFilepathStatesStudent = storageRef.child("ICS").child("States").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathStatesStudent.putBytes(dataStatesStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageStatesMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosStatesMetricResultCard = new ByteArrayOutputStream();
                bitmapImageStatesMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosStatesMetricResultCard);
                final byte[] dataStatesMetricResultCard = baosStatesMetricResultCard.toByteArray();

                StorageReference imageFilepathStatesMetricResultCard = storageRef.child("ICS").child("States").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathStatesMetricResultCard.putBytes(dataStatesMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageStatesFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosStatesFatherIDCard = new ByteArrayOutputStream();
                bitmapImageStatesFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosStatesFatherIDCard);
                final byte[] dataStatesFatherIDCard = baosStatesFatherIDCard.toByteArray();

                StorageReference imageFilepathStatesFatherIDCard = storageRef.child("ICS").child("States").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathStatesFatherIDCard.putBytes(dataStatesFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageStatesBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosStatesBill = new ByteArrayOutputStream();
                bitmapImageStatesBill.compress(Bitmap.CompressFormat.JPEG, 100, baosStatesBill);
                final byte[] dataStatesBill = baosStatesBill.toByteArray();

                StorageReference imageFilepathStatesBill = storageRef.child("ICS").child("States").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathStatesBill.putBytes(dataStatesBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageStatesSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosStatesSalary = new ByteArrayOutputStream();
                bitmapImageStatesSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosStatesSalary);
                final byte[] dataStatesSalary = baosStatesSalary.toByteArray();

                StorageReference imageFilepathStatesSalary = storageRef.child("ICS").child("States").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathStatesSalary.putBytes(dataStatesSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;
            case "Banking":
                Bitmap bitmapImageBankingStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosBankingStudent = new ByteArrayOutputStream();
                bitmapImageBankingStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosBankingStudent);
                final byte[] dataBankingStudent = baosBankingStudent.toByteArray();

                StorageReference imageFilepathBankingStudent = storageRef.child("ICOM").child("Banking").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathBankingStudent.putBytes(dataBankingStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageBankingMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosBankingMetricResultCard = new ByteArrayOutputStream();
                bitmapImageBankingMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosBankingMetricResultCard);
                final byte[] dataBankingMetricResultCard = baosBankingMetricResultCard.toByteArray();

                StorageReference imageFilepathBankingMetricResultCard = storageRef.child("ICOM").child("Banking").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathBankingMetricResultCard.putBytes(dataBankingMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageBankingFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosBankingFatherIDCard = new ByteArrayOutputStream();
                bitmapImageBankingFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosBankingFatherIDCard);
                final byte[] dataBankingFatherIDCard = baosBankingFatherIDCard.toByteArray();

                StorageReference imageFilepathBankingFatherIDCard = storageRef.child("ICOM").child("Banking").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathBankingFatherIDCard.putBytes(dataBankingFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageBankingBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosBankingBill = new ByteArrayOutputStream();
                bitmapImageBankingBill.compress(Bitmap.CompressFormat.JPEG, 100, baosBankingBill);
                final byte[] dataBankingBill = baosBankingBill.toByteArray();

                StorageReference imageFilepathBankingBill = storageRef.child("ICOM").child("Banking").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathBankingBill.putBytes(dataBankingBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageBankingSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosBankingSalary = new ByteArrayOutputStream();
                bitmapImageBankingSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosBankingSalary);
                final byte[] dataBankingSalary = baosBankingSalary.toByteArray();

                StorageReference imageFilepathBankingSalary = storageRef.child("ICOM").child("Banking").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathBankingSalary.putBytes(dataBankingSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;

            case "Commerce":
                Bitmap bitmapImageCommerceStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosCommerceStudent = new ByteArrayOutputStream();
                bitmapImageCommerceStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosCommerceStudent);
                final byte[] dataCommerceStudent = baosCommerceStudent.toByteArray();

                StorageReference imageFilepathCommerceStudent = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathCommerceStudent.putBytes(dataCommerceStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageCommerceMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosCommerceMetricResultCard = new ByteArrayOutputStream();
                bitmapImageCommerceMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosCommerceMetricResultCard);
                final byte[] dataCommerceMetricResultCard = baosCommerceMetricResultCard.toByteArray();

                StorageReference imageFilepathCommerceMetricResultCard = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathCommerceMetricResultCard.putBytes(dataCommerceMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageCommerceFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosCommerceFatherIDCard = new ByteArrayOutputStream();
                bitmapImageCommerceFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosCommerceFatherIDCard);
                final byte[] dataCommerceFatherIDCard = baosCommerceFatherIDCard.toByteArray();

                StorageReference imageFilepathCommerceFatherIDCard = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathCommerceFatherIDCard.putBytes(dataCommerceFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageCommerceBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosCommerceBill = new ByteArrayOutputStream();
                bitmapImageCommerceBill.compress(Bitmap.CompressFormat.JPEG, 100, baosCommerceBill);
                final byte[] dataCommerceBill = baosCommerceBill.toByteArray();

                StorageReference imageFilepathCommerceBill = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathCommerceBill.putBytes(dataCommerceBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageCommerceSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosCommerceSalary = new ByteArrayOutputStream();
                bitmapImageCommerceSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosCommerceSalary);
                final byte[] dataCommerceSalary = baosCommerceSalary.toByteArray();

                StorageReference imageFilepathCommerceSalary = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathCommerceSalary.putBytes(dataCommerceSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;
            case "IT":
                Bitmap bitmapImageITStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosITStudent = new ByteArrayOutputStream();
                bitmapImageITStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosITStudent);
                final byte[] dataITStudent = baosITStudent.toByteArray();

                StorageReference imageFilepathITStudent = storageRef.child("FA").child("IT").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathITStudent.putBytes(dataITStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageITMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosITMetricResultCard = new ByteArrayOutputStream();
                bitmapImageITMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosITMetricResultCard);
                final byte[] dataITMetricResultCard = baosITMetricResultCard.toByteArray();

                StorageReference imageFilepathITMetricResultCard = storageRef.child("FA").child("IT").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathITMetricResultCard.putBytes(dataITMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageITFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosITFatherIDCard = new ByteArrayOutputStream();
                bitmapImageITFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosITFatherIDCard);
                final byte[] dataITFatherIDCard = baosITFatherIDCard.toByteArray();

                StorageReference imageFilepathITFatherIDCard = storageRef.child("FA").child("IT").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathITFatherIDCard.putBytes(dataITFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageITBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosITBill = new ByteArrayOutputStream();
                bitmapImageITBill.compress(Bitmap.CompressFormat.JPEG, 100, baosITBill);
                final byte[] dataITBill = baosITBill.toByteArray();

                StorageReference imageFilepathITBill = storageRef.child("FA").child("IT").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathITBill.putBytes(dataITBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageITSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosITSalary = new ByteArrayOutputStream();
                bitmapImageITSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosITSalary);
                final byte[] dataITSalary = baosITSalary.toByteArray();

                StorageReference imageFilepathITSalary = storageRef.child("FA").child("IT").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathITSalary.putBytes(dataITSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;
            case "Education":
                Bitmap bitmapImageEducationStudent = ((BitmapDrawable) imageViewStudent.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEducationStudent = new ByteArrayOutputStream();
                bitmapImageEducationStudent.compress(Bitmap.CompressFormat.JPEG, 100, baosEducationStudent);
                final byte[] dataEducationStudent = baosEducationStudent.toByteArray();

                StorageReference imageFilepathEducationStudent = storageRef.child("FA").child("Education").child(currentUser).child("Student" + ".jpg");
//                Now putting image by using this path.
                imageFilepathEducationStudent.putBytes(dataEducationStudent).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("student_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEducationMetricResultCard = ((BitmapDrawable) imageViewMetricResultCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEducationMetricResultCard = new ByteArrayOutputStream();
                bitmapImageEducationMetricResultCard.compress(Bitmap.CompressFormat.JPEG, 100, baosEducationMetricResultCard);
                final byte[] dataEducationMetricResultCard = baosEducationMetricResultCard.toByteArray();

                StorageReference imageFilepathEducationMetricResultCard = storageRef.child("FA").child("Education").child(currentUser).child("Metric Result Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathEducationMetricResultCard.putBytes(dataEducationMetricResultCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("result_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEducationFatherIDCard = ((BitmapDrawable) imageViewFatherIdCard.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEducationFatherIDCard = new ByteArrayOutputStream();
                bitmapImageEducationFatherIDCard.compress(Bitmap.CompressFormat.JPEG, 100, baosEducationFatherIDCard);
                final byte[] dataEducationFatherIDCard = baosEducationFatherIDCard.toByteArray();

                StorageReference imageFilepathEducationFatherIDCard = storageRef.child("FA").child("Education").child(currentUser).child("Father's ID Card" + ".jpg");

//                Now putting image by using this path.
                imageFilepathEducationFatherIDCard.putBytes(dataEducationFatherIDCard).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("father_id_card_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEducationBill = ((BitmapDrawable) imageViewBill.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEducationBill = new ByteArrayOutputStream();
                bitmapImageEducationBill.compress(Bitmap.CompressFormat.JPEG, 100, baosEducationBill);
                final byte[] dataEducationBill = baosEducationBill.toByteArray();

                StorageReference imageFilepathEducationBill = storageRef.child("FA").child("Education").child(currentUser).child("Electricity Bill" + ".jpg");

//                Now putting image by using this path.
                imageFilepathEducationBill.putBytes(dataEducationBill).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("bill_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                    }
                                });
                            }
                        }
                    }
                });
                Bitmap bitmapImageEducationSalary = ((BitmapDrawable) imageViewSalary.getDrawable()).getBitmap();
                ByteArrayOutputStream baosEducationSalary = new ByteArrayOutputStream();
                bitmapImageEducationSalary.compress(Bitmap.CompressFormat.JPEG, 100, baosEducationSalary);
                final byte[] dataEducationSalary = baosEducationSalary.toByteArray();

                StorageReference imageFilepathEducationSalary = storageRef.child("FA").child("Education").child(currentUser).child("Salary Slip" + ".jpg");
//                Now putting image by using this path.
                imageFilepathEducationSalary.putBytes(dataEducationSalary).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String imageDownloadLink = task.getResult().getDownloadUrl().toString();
                            if (task.isSuccessful()) {
                                Map updateHashMap = new HashMap();
                                updateHashMap.put("salary_slip_image", imageDownloadLink);
                                dbRefAdmissions.updateChildren(updateHashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        if (b == 1) {
                                            progressDialogSubmit.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Submitted Successfully", Toast.LENGTH_SHORT).show();
                                            b = 0;
                                        } else {
                                            if (b == 0)
                                                progressDialogUpdate.dismiss();
                                            Toast.makeText(AdmissionFormActivity.this, "Form is Updated Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
                break;
        }
    }

    private void clearTextFields() {
        name.setText("");
        fatherName.setText("");
        fatherOccupation.setText("");
        monthlyIncome.setText("");
        phoneNo.setText("");
        mobile.setText("");
        studentEmail.setText("");
        postalCode.setText("");
        CNICorBayFormNo.setText("");
    }

    private void showSnackBar() {
        Snackbar mySnackbar = Snackbar.make(relativeLayoutForm, "Plz Fill All Fields & Insert Images", Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.WHITE).setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        View snackBarView = mySnackbar.getView();
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackBarView.setBackgroundColor(Color.parseColor("#BF360C"));
        mySnackbar.show();
    }

    private void getImage() {
//        CropImage ==> It is a library which is used to get image from external apps (e.g gallery or camera etc)
//        by executing intent automatically (e.g startActivityForResult).
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AdmissionFormActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == galleryPic && resultCode == RESULT_OK) {
//            Now coming picture is stored in image Uri.
            Uri image = data.getData();
//            Will crop the image.
            CropImage.activity(image).setAspectRatio(1, 1).start(AdmissionFormActivity.this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Now picture is stored in result.
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
//                resultUri has the current Image which is not compress yet(will store in image_filepath).
                Uri resultUri = result.getUri();
//                Now Picture is in fileImage and sending to the compression method (compressImage).
                File fileImage = new File(resultUri.getPath());
//                byteImageCompress has the Compressed image (will store in thumb_filepath).
                byte[] byteImageCompress = compressImage(fileImage);
//                Convert and set image to ImageView...
                Bitmap bitmapImage = BitmapFactory.decodeByteArray(byteImageCompress, 0, byteImageCompress.length);
                switch (buttonText) {
                    case "Student Image":
                        imageViewStudent.setImageBitmap(bitmapImage);
                        studentImage = true;
                        break;
                    case "Metric Result Card Image":
                        imageViewMetricResultCard.setImageBitmap(bitmapImage);
                        studentMetricResultCardImage = true;
                        break;
                    case "Father ID Card Image":
                        imageViewFatherIdCard.setImageBitmap(bitmapImage);
                        studentFatherIDCardImage = true;
                        break;
                    case "Bill Image":
                        imageViewBill.setImageBitmap(bitmapImage);
                        studentElectricityBillImage = true;
                        break;
                    case "Salary Image":
                        imageViewSalary.setImageBitmap(bitmapImage);
                        studentSalarySlipImage = true;
                        break;
                }
            }
        }
    }

    private byte[] compressImage(File imageFile) {
        Bitmap imageBitmap;
        byte[] thumbByte = new byte[0];
        try {
            imageBitmap = new Compressor(this).
                    setMaxWidth(200)
                    .setMaxHeight(200)
                    .setQuality(75)
                    .compressToBitmap(imageFile);
//      Creating byte Stream
//            Input/output Stream controls the input and output of data.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            Now outputStream data send and retrieve data in the form of byteArray.
            thumbByte = baos.toByteArray();
            return thumbByte;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return thumbByte;
    }

//    Retrieve the Form...

    public void buttonEditForm(View view) {

        if (!CheckInternetConnectivity.isConnected(AdmissionFormActivity.this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            a = 1;
            retrieveDataFromDB();
            showProgressDialog();

            buttonEditForm.setVisibility(View.GONE);
            buttonUpdateForm.setVisibility(View.VISIBLE);
            buttonDeleteForm.setVisibility(View.VISIBLE);
        }
    }

    private void retrieveDataFromDB() {
        dbRefAdmissions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (a == 1) {
//                if(dataSnapshot.hasChild("ccjjc")){
                    String dbFormNo = dataSnapshot.child("form_no").getValue().toString();
//                Now set this Text from Database to EditTexts...
                    formNo.setText(dbFormNo);
//            }
//            else{
//                    formNo.setText("1");
//                }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbRefAdmissions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (a == 1) {
//                Getting Text Values From Database...
                    String dbName = dataSnapshot.child("name").getValue().toString();
                    String dbFatherName = dataSnapshot.child("father_name").getValue().toString();
                    String dbFatherOccupation = dataSnapshot.child("father_occupation").getValue().toString();
                    String dbMonthlyIncome = dataSnapshot.child("monthly_income").getValue().toString();
                    String dbPhoneNo = dataSnapshot.child("phone_no").getValue().toString();
                    String dbMobile = dataSnapshot.child("mobile").getValue().toString();
                    String dbEmail = dataSnapshot.child("email").getValue().toString();
                    String dbPostalCode = dataSnapshot.child("postal_code").getValue().toString();
                    String dbStudentCNIC = dataSnapshot.child("student_cnic").getValue().toString();
//                Now set this Text from Database to EditTexts...
                    name.setText(dbName);
                    fatherName.setText(dbFatherName);
                    fatherOccupation.setText(dbFatherOccupation);
                    monthlyIncome.setText(dbMonthlyIncome);
                    phoneNo.setText(dbPhoneNo);
                    mobile.setText(dbMobile);
                    studentEmail.setText(dbEmail);
                    postalCode.setText(dbPostalCode);
                    CNICorBayFormNo.setText(dbStudentCNIC);

//                Now get All the images...

                    final String studentThumbImage = dataSnapshot.child("student_image").getValue().toString();
                    final String metricResultCardThumbImage = dataSnapshot.child("result_card_image").getValue().toString();
                    final String fatherIdCardThumbImage = dataSnapshot.child("father_id_card_image").getValue().toString();
                    final String billThumbImage = dataSnapshot.child("bill_image").getValue().toString();
                    final String salaryThumbImage = dataSnapshot.child("salary_slip_image").getValue().toString();

                    if (!studentThumbImage.equals("false")) {
                        Picasso.get().load(studentThumbImage).placeholder(R.drawable.student_place_holder).into(imageViewStudent, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(studentThumbImage).placeholder(R.drawable.student_place_holder).into(imageViewStudent);
                            }
                        });
                    }
                    if (!metricResultCardThumbImage.equals("false")) {
                        Picasso.get().load(metricResultCardThumbImage).placeholder(R.drawable.metric_rc_place_holder).into(imageViewMetricResultCard, new Callback() {
                            //                       Callback ==> Response
                            @Override
                            public void onSuccess() {
//                            Toast.makeText(AdmissionFormActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(metricResultCardThumbImage).placeholder(R.drawable.metric_rc_place_holder).into(imageViewMetricResultCard);
                            }
                        });
                    }
                    if (!fatherIdCardThumbImage.equals("false")) {
                        Picasso.get().load(fatherIdCardThumbImage).placeholder(R.drawable.father_id_card_place_holder).into(imageViewFatherIdCard, new Callback() {
                            //                       Callback ==> Response
                            @Override
                            public void onSuccess() {
//                            Toast.makeText(AdmissionFormActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                if (billThumbImage.equals("false")) {
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                if (billThumbImage.equals("false")) {
                                    progressDialog.dismiss();
                                }
                                Picasso.get().load(fatherIdCardThumbImage).placeholder(R.drawable.father_id_card_place_holder).into(imageViewFatherIdCard);
                            }
                        });
                    }
                    if (!billThumbImage.equals("false")) {
                        Picasso.get().load(billThumbImage).placeholder(R.drawable.bill_place_holder).into(imageViewBill, new Callback() {
                            //                       Callback ==> Response
                            @Override
                            public void onSuccess() {
//                            Toast.makeText(AdmissionFormActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(billThumbImage).placeholder(R.drawable.bill_place_holder).into(imageViewBill);
                            }
                        });
                    }
                    if (!salaryThumbImage.equals("false")) {
                        Picasso.get().load(salaryThumbImage).placeholder(R.drawable.salary_slip_place_holder).into(imageViewSalary, new Callback() {
                            //                       Callback ==> Response
                            @Override
                            public void onSuccess() {
//                            Toast.makeText(AdmissionFormActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onError(Exception e) {
                                progressDialog.dismiss();
                                Picasso.get().load(salaryThumbImage).placeholder(R.drawable.salary_slip_place_holder).into(imageViewSalary);
                            }
                        });
                    }
                }
                a = 0;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    Update the Form...

    public void buttonUpdateForm(View view) {
        b = 0;
        updateForm();
    }

    private void updateForm() {
        Boolean response = validateTextData();
        if (!response) {
            showSnackBar();
        } else {
            if (!studentImage) {
                showSnackBar();
            } else {
                String spinnerValue = spinner.getSelectedItem().toString();
                switch (spinnerValue) {
                    case "Choose Scholarship Program":
                        showSnackBar();
                        break;
                    case "Merit Based":
                        if (!studentMetricResultCardImage || !studentFatherIDCardImage) {
                            showSnackBar();
                        } else {
                            if (!CheckInternetConnectivity.isConnected(AdmissionFormActivity.this)) {
//                                Toast.makeText(this, "" + CheckInternetConnectivity.isConnected(AdmissionFormActivity.this), Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            } else {
                                submitTextForm();
                                submitAllImages();
                                showProgressDialogUpdate();

                                imageViewStudent.setImageResource(R.drawable.student_place_holder);
                                imageViewMetricResultCard.setImageResource(R.drawable.metric_rc_place_holder);
                                imageViewFatherIdCard.setImageResource(R.drawable.father_id_card_place_holder);
                                clearTextFields();

                                buttonUpdateForm.setVisibility(View.GONE);
                                buttonEditForm.setVisibility(View.VISIBLE);
                                buttonDeleteForm.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    case "Needy Based":
                        if (!studentMetricResultCardImage || !studentFatherIDCardImage ||
                                !studentElectricityBillImage || !studentSalarySlipImage) {
                            showSnackBar();
                        } else {
                            if (!CheckInternetConnectivity.isConnected(AdmissionFormActivity.this)) {
//                                Toast.makeText(this, "" + CheckInternetConnectivity.isConnected(AdmissionFormActivity.this), Toast.LENGTH_SHORT).show();
                                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                            } else {

                                submitTextForm();
                                submitAllImages();
                                showProgressDialogUpdate();

                                imageViewStudent.setImageResource(R.drawable.student_place_holder);
                                imageViewMetricResultCard.setImageResource(R.drawable.metric_rc_place_holder);
                                imageViewFatherIdCard.setImageResource(R.drawable.father_id_card_place_holder);
                                imageViewBill.setImageResource(R.drawable.bill_place_holder);
                                imageViewSalary.setImageResource(R.drawable.salary_slip_place_holder);
                                clearTextFields();

                                buttonUpdateForm.setVisibility(View.GONE);
                                buttonEditForm.setVisibility(View.VISIBLE);
                                buttonDeleteForm.setVisibility(View.VISIBLE);
                            }
                        }
                        break;
                    default:
                        Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    public void buttonDeleteForm(View view) {
        if (!CheckInternetConnectivity.isConnected(AdmissionFormActivity.this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            deleteFormAlertDialog();
        }
    }

    private void deleteFormAlertDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AdmissionFormActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Do you want to Delete your Admission?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteForm();
                b = 1;
                buttonSubmitForm.setVisibility(View.VISIBLE);
                buttonEditForm.setVisibility(View.GONE);
                buttonUpdateForm.setVisibility(View.GONE);
                buttonDeleteForm.setVisibility(View.GONE);
                Toast.makeText(AdmissionFormActivity.this, "Form is Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void deleteForm() {
        clearTextFields();
        imageViewStudent.setImageResource(R.drawable.student_place_holder);
        imageViewMetricResultCard.setImageResource(R.drawable.metric_rc_place_holder);
        imageViewFatherIdCard.setImageResource(R.drawable.father_id_card_place_holder);
        imageViewBill.setImageResource(R.drawable.bill_place_holder);
        imageViewSalary.setImageResource(R.drawable.salary_slip_place_holder);

//        Get Form No To reduce it by -1 because admission is deleted.
        int formNo = getFormNoForDecrement();
        formNo = formNo - 1;
        dbRefFormNumber.child("total").setValue(formNo);
        getFormNoFromDB();
//        To remove Text Forms...
        dbRefAdmissions.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//        To remove Images...
                switch (stringStudyProgram) {
                    case "Pre_Medical": {
                        StorageReference delStudentImageRef = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("FSC").child("PreMedical").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "Pre_Engineering": {
                        StorageReference delStudentImageRef = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("FSC").child("PreEngineering").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "Physics": {
                        StorageReference delStudentImageRef = storageRef.child("ICS").child("Physics").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("ICS").child("Physics").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("ICS").child("Physics").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("ICS").child("Physics").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("ICS").child("Physics").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "States": {
                        StorageReference delStudentImageRef = storageRef.child("ICS").child("States").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("ICS").child("States").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("ICS").child("States").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("ICS").child("States").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("ICS").child("States").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "Banking": {
                        StorageReference delStudentImageRef = storageRef.child("ICOM").child("Banking").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("ICOM").child("Banking").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("ICOM").child("Banking").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("ICOM").child("Banking").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("ICOM").child("Banking").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "Commerce": {
                        StorageReference delStudentImageRef = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("ICOM").child("Commerce").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "Education": {
                        StorageReference delStudentImageRef = storageRef.child("FA").child("Education").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("FA").child("Education").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("FA").child("Education").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("FA").child("Education").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("FA").child("Education").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                    case "IT": {
                        StorageReference delStudentImageRef = storageRef.child("FA").child("IT").child(currentUser).child("Student" + ".jpg");
                        delStudentImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Student Pic Deleted.");
                            }
                        });
                        StorageReference delMetricResultCardImageRef = storageRef.child("FA").child("IT").child(currentUser).child("Metric Result Card" + ".jpg");
                        delMetricResultCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Metric Result Card Pic Deleted.");
                            }
                        });
                        StorageReference delFatherIdCardImageRef = storageRef.child("FA").child("IT").child(currentUser).child("Father's ID Card" + ".jpg");
                        delFatherIdCardImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delElectricityBillImageRef = storageRef.child("FA").child("IT").child(currentUser).child("Electricity Bill" + ".jpg");
                        delElectricityBillImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                        StorageReference delSalarySlipImageRef = storageRef.child("FA").child("IT").child(currentUser).child("Salary Slip" + ".jpg");
                        delSalarySlipImageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("LOG", "Father's ID Card Pic Deleted.");
                            }
                        });
                    }
                }
            }
        });
    }

    public int getFormNoForDecrement() {
        dbRefFormNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.hasChild("form_noo")) {
                getFormNo = Integer.parseInt(dataSnapshot.child("total").getValue().toString());
//                Toast.makeText(AdmissionFormActivity.this, "Form no is " + getFormNo, Toast.LENGTH_SHORT).show();
            }
// else {
//                    formNo.setText("1");
//                }
//            }
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return getFormNo;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(AdmissionFormActivity.this);
        progressDialog.setMessage("Retrieving Form...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void showProgressDialogSubmit() {
        progressDialogSubmit = new ProgressDialog(AdmissionFormActivity.this);
        progressDialogSubmit.setMessage("Submitting Form...");
        progressDialogSubmit.setCancelable(false);
        progressDialogSubmit.show();
    }

    private void showProgressDialogUpdate() {
        progressDialogUpdate = new ProgressDialog(AdmissionFormActivity.this);
        progressDialogUpdate.setMessage("Updating Form...");
        progressDialogUpdate.setCancelable(false);
        progressDialogUpdate.show();
    }
}