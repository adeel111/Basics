package com.example.adeeliftikhar.sqlitedb;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeeliftikhar.sqlitedb.Database.DatabaseOperations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UploadImageActivity extends AppCompatActivity {
    SQLiteDatabase sqLiteDatabase;
    DatabaseOperations databaseOperations;
    ImageView cameraImage, galleryImage;

    TextView textViewCameraImage, textViewGalleryImage;
    Button buttonTakeImage, buttonInsertImage, buttonShowImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        databaseOperations = new DatabaseOperations(UploadImageActivity.this);
        cameraImage = findViewById(R.id.camera_image);
        galleryImage = findViewById(R.id.gallery_image);

        textViewCameraImage = findViewById(R.id.text_view_camera_image);
        textViewGalleryImage = findViewById(R.id.text_view_gallery_image);
        buttonTakeImage = findViewById(R.id.button_take_image);
        buttonInsertImage = findViewById(R.id.button_insert_image);
        buttonShowImage = findViewById(R.id.button_show_image);

//        textViewCameraImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

//        textViewGalleryImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
        buttonTakeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });

        buttonInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Used to insert drawable values...
//                Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.web);
//                byte[] cameraImageByte = getBitmapAsByteArray(bitmapImage);
//                byte[] galleryImageByte = getBitmapAsByteArray(bitmapImage);

//                Used to insert camera, gallery image...
                Bitmap bitmapCameraImage = ((BitmapDrawable) cameraImage.getDrawable()).getBitmap();
                Bitmap bitmapGalleryImage = ((BitmapDrawable) galleryImage.getDrawable()).getBitmap();
                byte[] cameraImageByte = getBitmapAsByteArray(bitmapCameraImage);
                byte[] galleryImageByte = getBitmapAsByteArray(bitmapGalleryImage);
//                Toast.makeText(UploadImageActivity.this, cameraImageByte.toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(UploadImageActivity.this, galleryImageByte.toString(), Toast.LENGTH_SHORT).show();
                sqLiteDatabase = databaseOperations.getWritableDatabase();
                databaseOperations.insertImage(cameraImageByte, galleryImageByte, sqLiteDatabase);
                Toast.makeText(UploadImageActivity.this, "Values are Inserted", Toast.LENGTH_SHORT).show();
                cameraImage.setImageBitmap(null);
                galleryImage.setImageBitmap(null);
            }
        });
        buttonShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDatabase = databaseOperations.getReadableDatabase();
                Cursor cursor = databaseOperations.getStudentImage(sqLiteDatabase);
                if (cursor != null && cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            byte[] imgCamera = cursor.getBlob(0);
                            byte[] imgGallery = cursor.getBlob(1);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imgCamera, 0, imgCamera.length);
                            Bitmap bitmap1 = BitmapFactory.decodeByteArray(imgGallery, 0, imgCamera.length);
                            cameraImage.setImageBitmap(bitmap);
                            galleryImage.setImageBitmap(bitmap1);
                        } while (cursor.moveToNext());
                    }
                }
            }
        });
    }

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();

    }

    public void takeImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadImageActivity.this);
        builder.setTitle("Take Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")) {
                    Intent intentOne = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentOne, 1);
                } else if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "SELECT_FILE"), 2);
                } else if (items[i].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
//                byte[] cameraImageByte = getBitmapAsByteArray(bitmap);
                cameraImage.setImageBitmap(bitmap);
            } else if (requestCode == 2) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(UploadImageActivity.this.getContentResolver(), selectedImageUri);
//                    byte[] bitmapGallery = getBitmapAsByteArray(bitmap);
                    galleryImage.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                galleryImage.setImageURI(selectedImageUri);
            }
        }
    }
}
