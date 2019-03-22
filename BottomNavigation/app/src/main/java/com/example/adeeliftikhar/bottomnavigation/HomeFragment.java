package com.example.adeeliftikhar.bottomnavigation;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private ViewPager viewPager;

    @Nullable
    @Override
    public Context getContext() {
        return super.getContext();
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.view_pager);
        GooglePlusFragmentPageAdapterOne adapter = new GooglePlusFragmentPageAdapterOne(getChildFragmentManager());
        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private static class GooglePlusFragmentPageAdapterOne extends FragmentPagerAdapter {

        private GooglePlusFragmentPageAdapterOne(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TabOneFragment.newInstance();
                case 1:
                    return TabTwoFragment.newInstance();
                case 2:
                    return TabThreeFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
//                View view = LayoutInflater.from().inflate(R.layout.tab_one_design,null);
//                return view;
                return "First";
            } else if (position == 1) {
                return "Second";
            } else if (position == 2) {
                return "Third";
            } else {
                return "Title";
            }
        }
    }
}
