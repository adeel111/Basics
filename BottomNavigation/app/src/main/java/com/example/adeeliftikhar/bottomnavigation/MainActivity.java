package com.example.adeeliftikhar.bottomnavigation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TabSectionsPagerAdapter myPagerAdapter;
    private ViewPager mViewPager;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myPagerAdapter = new TabSectionsPagerAdapter(getSupportFragmentManager());

//       Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(myPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        loadFragment(new HomeFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener);
    }

    // Loading default fragment...
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

//    Switching Fragments...

    private BottomNavigationView.OnNavigationItemSelectedListener myOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    break;

                case R.id.navigation_dashboard:
                    fragment = new DashBoardFragment();
                    break;

                case R.id.navigation_notifications:
                    fragment = new NotificationsFragment();
                    break;

                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };


//    <android.support.design.widget.BottomNavigationView
//    android:id="@+id/bottom_navigation_view"
//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"
//    android:layout_gravity="bottom"
//    app:itemBackground="@color/colorPrimary"
//    app:itemIconTint="@drawable/bottom_navigation_selector"
//    app:itemTextColor="@drawable/bottom_navigation_selector"
//    app:menu="@menu/navigation" />

}

