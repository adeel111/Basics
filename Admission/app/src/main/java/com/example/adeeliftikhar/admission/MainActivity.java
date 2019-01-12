package com.example.adeeliftikhar.admission;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.adeeliftikhar.admission.SessionsManager.LoginSessionManager;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleButton;
    private NavigationView navigationView;
    LoginSessionManager loginSessionManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbarSet();
        drawerLayoutSet();
        navigationViewWork();
    }

    private void toolbarSet() {
//        Custom Toolbar used as ActionBar...
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void drawerLayoutSet() {
//        Navigation Drawer...
        drawerLayout = findViewById(R.id.drawer_layout);
        toggleButton = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.text_open, R.string.text_close);
        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();
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
                    case R.id.fee_information:
                        Intent intentInformation = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                        String information = "information";
                        intentInformation.putExtra("id", information);
                        startActivity(intentInformation);
                        break;
                    case R.id.merit_list:
                        Intent intentList = new Intent(MainActivity.this, MeritListsActivity.class);
                        String list = "list";
                        intentList.putExtra("id", list);
                        startActivity(intentList);
                        break;
                }
                return true;
            }
        });
    }

    public void buttonSelectProgram(View view) {
        Button button = (Button) view;
        popupMenu(button);
    }

    private void popupMenu(Button button) {
//          Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(MainActivity.this, button);
        int id = button.getId();
        switch (id) {
            case R.id.button_fsc:
                popup.getMenuInflater().inflate(R.menu.fsc_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.fsc_medical:
                                Intent intentMedical = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programMedical = "Pre_Medical";
                                intentMedical.putExtra("idProgram", programMedical);
                                startActivity(intentMedical);
                                break;
                            case R.id.fsc_engineering:
                                Intent intentEngineering = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programEngineering = "Pre_Engineering";
                                intentEngineering.putExtra("idProgram", programEngineering);
                                startActivity(intentEngineering);
                                break;
                        }
                        return true;
                    }
                });
                break;
            case R.id.button_ics:
                popup.getMenuInflater().inflate(R.menu.ics_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.ics_physics:
                                Intent intentPhysics = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programPhysics = "Physics";
                                intentPhysics.putExtra("idProgram", programPhysics);
                                startActivity(intentPhysics);
                                break;
                            case R.id.ics_state:
                                Intent intentState = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programState = "States";
                                intentState.putExtra("idProgram", programState);
                                startActivity(intentState);
                                break;
                        }
                        return true;
                    }
                });
                break;
            case R.id.button_icom:
                popup.getMenuInflater().inflate(R.menu.icom_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.icom_banking:
                                Intent intentBanking = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programBanking = "Banking";
                                intentBanking.putExtra("idProgram", programBanking);
                                startActivity(intentBanking);
                                break;
                            case R.id.icom_second:
                                Intent intentSecond = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programSecond = "Commerce";
                                intentSecond.putExtra("idProgram", programSecond);
                                startActivity(intentSecond);
                                break;
                        }
                        return true;
                    }
                });
                break;
            case R.id.button_fa:
                popup.getMenuInflater().inflate(R.menu.fa_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.fa_it:
                                Intent intentIT = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programIT = "IT";
                                intentIT.putExtra("idProgram", programIT);
                                startActivity(intentIT);
                                break;
                            case R.id.fa_education:
                                Intent intentEducation = new Intent(MainActivity.this, AdmissionFormActivity.class);
                                String programEducation = "Education";
                                intentEducation.putExtra("idProgram", programEducation);
                                startActivity(intentEducation);
                                break;
                        }
                        return true;
                    }
                });
                break;
        }
        popup.show();
    }

//    Drawer Button Click...

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (toggleButton.onOptionsItemSelected(item)) {
            return true;
        }
        if (id == R.id.logout) {
            logoutAlertBox();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutAlertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout");
        builder.setMessage("Do you want to logout?");
        builder.setPositiveButton("logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogBoxLogout();
                logout();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void dialogBoxLogout() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Logout");
        progressDialog.setMessage("Logout in Progress, Plz Wait.");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void logout() {
        LoginSessionManager loginSessionManager = new LoginSessionManager(MainActivity.this);
        loginSessionManager.loginTheUser(false, "", "");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        progressDialog.dismiss();
        startActivity(intent);
        finish();
    }
}
