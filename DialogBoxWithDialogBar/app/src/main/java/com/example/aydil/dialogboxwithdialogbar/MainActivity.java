package com.example.aydil.dialogboxwithdialogbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private Button dialogButton;
    private Button alertDialogButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alertDialogButton = findViewById(R.id.alert_dialog_button);
        alertDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new SpotsDialog(MainActivity.this, R.style.Custom);
                alertDialog.show();
                alertDialog.dismiss();
            }
        });
        dialogButton = findViewById(R.id.dialog_box_button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMax(100);
                progressDialog.setMessage("Its Loading...");
                progressDialog.setTitle("Download");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (progressDialog.getProgress() <= progressDialog.getMax()) {
                                Thread.sleep(200);
                                handler.sendMessage(handler.obtainMessage());
                                if (progressDialog.getProgress() == progressDialog.getMax()) {
                                    progressDialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    progressDialog.incrementProgressBy(1);
                }
            };
        });
    }
}