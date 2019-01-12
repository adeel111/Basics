package com.example.adeeliftikhar.admission.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.adeeliftikhar.admission.Fragments.FAMeritListFragment;
import com.example.adeeliftikhar.admission.Fragments.FSCMeritListFragment;
import com.example.adeeliftikhar.admission.Fragments.ICOMMeritListFragment;
import com.example.adeeliftikhar.admission.Fragments.ICSMeritListFragment;

public class TabSectionsPagerAdapter extends FragmentPagerAdapter {
    public TabSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FSCMeritListFragment contactsFragment = new FSCMeritListFragment();
                return contactsFragment;
            case 1:
                ICSMeritListFragment chatFragment = new ICSMeritListFragment();
                return chatFragment;
            case 2:
                ICOMMeritListFragment callFragment = new ICOMMeritListFragment();
                return callFragment;
            case 3:
                FAMeritListFragment historyFragment = new FAMeritListFragment();
                return historyFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
