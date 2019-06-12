package com.example.adeeliftikhar.ambulancetracker;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private Toolbar toolbar;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

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

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

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

    private BottomNavigationView.OnNavigationItemSelectedListener myOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.bottom_nav_home:
                    toolbar.setTitle("Ready to Serve");
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.bottom_nav_history:
                    toolbar.setTitle(bottomNavigationView.getMenu().getItem(1).getTitle());
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.bottom_nav_settings:
                    toolbar.setTitle(bottomNavigationView.getMenu().getItem(2).getTitle());
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.bottom_nav_about:
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
                    return HistoryFragment.newInstance();
                case 2:
                    return SettingsFragment.newInstance();
                case 3:
                    return AboutFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}