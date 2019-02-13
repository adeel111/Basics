package com.example.aydil.snackbarandfab;

import android.graphics.Color;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private CoordinatorLayout mCoordinatorLayout;
    private Button btnSnackBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoordinatorLayout =  findViewById(R.id.coordinator_layout);
        btnSnackBar =  findViewById(R.id.btn_snack_bar);
        btnSnackBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnackBar();
            }
        });
    }

    private void showSnackBar() {
        Snackbar snackBar = Snackbar.make(mCoordinatorLayout,"This is the Snack Bar", Snackbar.LENGTH_INDEFINITE)
                .setAction("Goto", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Snackbar snackBar1 = Snackbar.make(mCoordinatorLayout, "Second Snack Bar", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Dismiss", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                                        snackBar1.dismiss();
                                    }
                                })
                                .setActionTextColor(Color.RED);
                        View snackView = snackBar1.getView();
                        TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.GREEN);
                        snackBar1.show();
                    }
                })
                .setActionTextColor(Color.GREEN);
        View snackView = snackBar.getView();
        TextView textView = snackView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackBar.show();
    }
}