package com.example.adeeliftikhar.bottomnavigation;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

//        loadFragment(new HomeFragment());

        viewPager = findViewById(R.id.view_pager);
        GooglePlusFragmentPageAdapter adapter = new GooglePlusFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        final View touchView = viewPager;

        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
//        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener);
        initTitle();
    }

    private void initTitle() {
        toolbar.post(new Runnable() {
            @Override
            public void run() {
                toolbar.setTitle(bottomNavigationView.getMenu().getItem(0).getTitle());
            }
        });
    }
//    private boolean loadFragment(Fragment fragment) {
//        if (fragment != null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
//            return true;
//        }
//        return false;
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener myOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle(bottomNavigationView.getMenu().getItem(0).getTitle());
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    toolbar.setTitle(bottomNavigationView.getMenu().getItem(1).getTitle());
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    toolbar.setTitle(bottomNavigationView.getMenu().getItem(2).getTitle());
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_profile:
                    toolbar.setTitle(bottomNavigationView.getMenu().getItem(3).getTitle());
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    private static class GooglePlusFragmentPageAdapter extends FragmentPagerAdapter {
        private GooglePlusFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return DashBoardFragment.newInstance();
                case 2:
                    return NotificationsFragment.newInstance();
                case 3:
                    return ProfileFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}


