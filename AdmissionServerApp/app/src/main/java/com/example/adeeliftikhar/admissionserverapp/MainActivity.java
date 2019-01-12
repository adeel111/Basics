package com.example.adeeliftikhar.admissionserverapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggleButton;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbarSet();
        drawerLayoutSet();
        navigationViewWork();
    }

    //        End of onCreate Method.
    private void toolbarSet() {
//        Custom Toolbar used as ActionBar...
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void drawerLayoutSet() {
//        Navigation Drawer...
        drawerLayout = findViewById(R.id.drawer_layout);
        toggleButton = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.text_open, R.string.text_close);
        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();
    }

    //    Drawer Button Click...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggleButton.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigationViewWork() {
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.chairman_message:
                        Intent intentMessage = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String message = "message";
                        intentMessage.putExtra("id", message);
                        startActivity(intentMessage);
                        break;
                    case R.id.team:
                        Intent intentTeam = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String team = "team";
                        intentTeam.putExtra("id", team);
                        startActivity(intentTeam);
                        break;
                    case R.id.history:
                        Intent intentHistory = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String history = "history";
                        intentHistory.putExtra("id", history);
                        startActivity(intentHistory);
                        break;
                    case R.id.schedule:
                        Intent intentSchedule = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String schedule = "schedule";
                        intentSchedule.putExtra("id", schedule);
                        startActivity(intentSchedule);
                        break;
                    case R.id.rules:
                        Intent intentRules = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String rules = "rules";
                        intentRules.putExtra("id", rules);
                        startActivity(intentRules);
                        break;
                    case R.id.facility:
                        Intent intentFacility = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String facility = "facility";
                        intentFacility.putExtra("id", facility);
                        startActivity(intentFacility);
                        break;
                    case R.id.activities:
                        Intent intentActivities = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String activities = "activities";
                        intentActivities.putExtra("id", activities);
                        startActivity(intentActivities);
                        break;
                    case R.id.scholarship_info:
                        Intent intentInformation = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String information = "information";
                        intentInformation.putExtra("id", information);
                        startActivity(intentInformation);
                        break;
                    case R.id.merit_list:
                        Intent intentList = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String list = "list";
                        intentList.putExtra("id", list);
                        startActivity(intentList);
                        break;
                }
                return true;
            }
        });
    }

    public void seeAdmissions(View view) {
    }
}
