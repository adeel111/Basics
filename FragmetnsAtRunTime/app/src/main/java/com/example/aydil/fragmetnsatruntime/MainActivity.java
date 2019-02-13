package com.example.aydil.fragmetnsatruntime;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
//    A FragmentManager manages Fragments in Android, specifically it handles transactions between fragments.
    public static FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        To add a fragment we have to make an object of FragmentManager class as below.
        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.container_layout) != null)
//        It will check either the container (activity_main) is available or not...
        {
            if(savedInstanceState != null)
//            It means activity is resumed(activity life_cycle event) and fragment is already added.
//            It prevents from overlapping fragments.
            {
                return;
            }
//            A transaction is a way to add, replace, or remove fragments.
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            Adding Fragment... 1st create an object of Fragment Activity.
            HomeFragment homeFragment = new HomeFragment();
            fragmentTransaction.add(R.id.container_layout,homeFragment,null);
            fragmentTransaction.commit();


        }
    }
}
