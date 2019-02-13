package com.example.aydil.fragmenttofragmentcommunication;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MessageFragment.OnMessageSendListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null)
         {
            if (savedInstanceState != null)
                return;
        }
        MessageFragment messageFragment = new MessageFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, messageFragment, null).commit();

    }

    @Override
    public void onMessageSend(String message) {
        DisplayFragment displayFragment = new DisplayFragment();
//        Bundle is used to pass data between Activities.
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
//        Parse bundle to Fragment reference variable.
        displayFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,displayFragment,null);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
