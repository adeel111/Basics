package com.example.adeeliftikhar.bottomnavigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabSectionsPagerAdapter extends FragmentPagerAdapter {
    public TabSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabOneFragment oneFragment = new TabOneFragment();
                return oneFragment;
            case 1:
                TabTwoFragment TwoFragment = new TabTwoFragment();
                return TwoFragment;
            case 2:
                TabThreeFragment threeFragment = new TabThreeFragment();
                return threeFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

}
