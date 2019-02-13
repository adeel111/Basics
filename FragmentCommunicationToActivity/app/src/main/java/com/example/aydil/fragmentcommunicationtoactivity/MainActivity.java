package com.example.aydil.fragmentcommunicationtoactivity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MessageFragment.OnMessageReadListener {
    //    public static FragmentManager fragmentManager;
    private TextView textViewForMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        fragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null)
                return;
        }

//        First Way to Add Fragment.

        MessageFragment messageFragment = new MessageFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, messageFragment, null);
        fragmentTransaction.commit();

//        Second Way to Add Fragment (un-comment all code comments.).

//        MessageFragment messageFragment = new MessageFragment();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.fragment_container,messageFragment,null);
//        fragmentTransaction.commit();
    }

    @Override
    public void onMessageRead(String message) {
        textViewForMessage = findViewById(R.id.text_view_for_message);
        textViewForMessage.setText(message);
    }
}
