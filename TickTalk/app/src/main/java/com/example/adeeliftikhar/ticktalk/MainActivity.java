package com.example.adeeliftikhar.ticktalk;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.example.adeeliftikhar.ticktalk.Fragments.ChatFragment;
import com.example.adeeliftikhar.ticktalk.Fragments.FriendsFragment;
import com.example.adeeliftikhar.ticktalk.Fragments.RequestFragment;
import com.example.adeeliftikhar.ticktalk.LoginSessionPkg.SessionManagerClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {
    SessionManagerClass sessionManagerClass;
    DatabaseReference mUserDbRef;
    FirebaseAuth mAuth;
    private SectionsPagerAdapter mSectionsPagerAdapter;
//    The ViewPager is the widget that allows the user to swipe left or right to see an entirely new screen.
//    In a sense, it's just a nicer way to show the user multiple tabs. It also has the ability to dynamically
//    add and remove pages (or tabs) at anytime.
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        mUserDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.all_users) {
            Intent intent = new Intent(MainActivity.this, AllUsersActivity.class);
            startActivity(intent);
        }
        if (id == R.id.profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            sessionManagerClass = new SessionManagerClass(MainActivity.this);
            sessionManagerClass.loginTheUser(false, "", "");
        }
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ChatFragment contactsFragment = new ChatFragment();
                    return contactsFragment;
                case 1:
                    FriendsFragment chatFragment = new FriendsFragment();
                    return chatFragment;
                case 2:
                    RequestFragment callFragment = new RequestFragment();
                    return callFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
//            finish();
        } else {
            mUserDbRef.child("online").setValue("true");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mUserDbRef.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
}
