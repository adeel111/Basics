package com.example.adeeliftikhar.admissionserverapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.adeeliftikhar.admissionserverapp.Fragments.AdmissionScheduleFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.CampusFacilityFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.ChairmanMessageFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.CurricularActivitiesFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.MeritListFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.RulesAndRegulationsFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.ScholarshipInforamtionFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.SuperiorHistoryFragment;
import com.example.adeeliftikhar.admissionserverapp.Fragments.SuperiorTeamFragment;

public class FragmentsContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments_container);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
//        Calling Method which will get the data from Intent.
        getIntentMethod();
    }

    private void getIntentMethod() {
        Intent intent = getIntent();
        String data = intent.getStringExtra("id");
//        Passing data to a method which will Replace the Fragment on the base of data
        fragmentsSetter(data);
    }

    private void fragmentsSetter(String dataId) {
        if (dataId.equals("message")) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragments_container, new ChairmanMessageFragment()).commit();
            getSupportActionBar().setTitle("Chairman Message");
        }
        if (dataId.equals("team")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new SuperiorTeamFragment()).commit();
            getSupportActionBar().setTitle("Superior Team");
        }
        if (dataId.equals("history")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new SuperiorHistoryFragment()).commit();
            getSupportActionBar().setTitle("Superior History");
        }
        if (dataId.equals("schedule")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new AdmissionScheduleFragment()).commit();
            getSupportActionBar().setTitle("Admission Schedule");
        }
        if (dataId.equals("rules")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new RulesAndRegulationsFragment()).commit();
            getSupportActionBar().setTitle("Rules and Regulations");
        }
        if (dataId.equals("facility")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new CampusFacilityFragment()).commit();
            getSupportActionBar().setTitle("Campus Facility");
        }

        if (dataId.equals("activities")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new CurricularActivitiesFragment()).commit();
            getSupportActionBar().setTitle("Curricular Activities");
        }
        if (dataId.equals("information")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new ScholarshipInforamtionFragment()).commit();
            getSupportActionBar().setTitle("Scholarship Information");
        }
        if (dataId.equals("list")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragments_container, new MeritListFragment()).commit();
            getSupportActionBar().setTitle("Merit List");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
