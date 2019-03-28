package com.example.adeeliftikhar.admissionserverapp;

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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.adeeliftikhar.admissionserverapp.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.admissionserverapp.SessionManager.LoginSessionManager;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggleButton;
    LoginSessionManager loginSessionManager;
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

    private void navigationViewWork() {
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.chairman_message:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentMessage = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String message = "message";
                            intentMessage.putExtra("id", message);
                            startActivity(intentMessage);
                        }
                        break;
                    case R.id.team:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentTeam = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String team = "team";
                            intentTeam.putExtra("id", team);
                            startActivity(intentTeam);
                        }
                        break;
                    case R.id.history:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentHistory = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String history = "history";
                            intentHistory.putExtra("id", history);
                            startActivity(intentHistory);
                        }
                        break;
                    case R.id.schedule:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentSchedule = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String schedule = "schedule";
                            intentSchedule.putExtra("id", schedule);
                            startActivity(intentSchedule);
                        }
                        break;
                    case R.id.rules:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentRules = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String rules = "rules";
                            intentRules.putExtra("id", rules);
                            startActivity(intentRules);
                        }
                        break;
                    case R.id.facility:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentFacility = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String facility = "facility";
                            intentFacility.putExtra("id", facility);
                            startActivity(intentFacility);
                        }
                        break;
                    case R.id.activities:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentActivities = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String activities = "activities";
                            intentActivities.putExtra("id", activities);
                            startActivity(intentActivities);
                        }
                        break;
                    case R.id.scholarship_info:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentInformation = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String information = "information";
                            intentInformation.putExtra("id", information);
                            startActivity(intentInformation);
                        }
                        break;
                    case R.id.merit_list:
                        if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intentList = new Intent(MainActivity.this, FragmentsContainerActivity.class);
                            String list = "list";
                            intentList.putExtra("id", list);
                            startActivity(intentList);
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void seeAdmissions(View view) {
    }

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
        builder.setMessage("Are you sure to Logout?");
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!CheckInternetConnectivity.isConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    logout();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void logout() {
        LoginSessionManager loginSessionManager = new LoginSessionManager(MainActivity.this);
        loginSessionManager.loginTheUser(false, "", "");
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, AdminLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
