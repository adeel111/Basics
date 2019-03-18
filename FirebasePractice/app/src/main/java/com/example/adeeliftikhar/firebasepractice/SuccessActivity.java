package com.example.adeeliftikhar.firebasepractice;

import android.database.DataSetObserver;
import android.net.wifi.WifiConfiguration;
import android.os.RemoteException;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adeeliftikhar.firebasepractice.Adapter.ListAdapterClass;
import com.google.android.gms.auth.api.Auth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SuccessActivity extends AppCompatActivity {
    private Spinner spinner;
    private ListView listView;
    private ListAdapterClass listAdapterClass;
    private String name, email;

    private String[] nameArray;
    private String[] emailArray;

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    String currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("GoogleUsers").child(currentUser);

        listView = findViewById(R.id.list_view);
        spinner = findViewById(R.id.spinner);
        getDataFromFirebase();
    }

    private void getDataFromFirebase() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> arrayList = new ArrayList<>();
                final ArrayList<String> arrayListName = new ArrayList<>();
                final ArrayList<String> arrayListEmail = new ArrayList<>();

                for (DataSnapshot nameSnapshot : dataSnapshot.getChildren()) {
                    name = dataSnapshot.child("name").getValue(String.class);
                    email = dataSnapshot.child("email").getValue(String.class);

                    if (name != null && email != null) {
                        arrayList.add(name);
                        arrayList.add(email);
                    }

                    if (name != null) {
                        arrayListName.add(name);
                        if (email != null) {
                            arrayListEmail.add(email);
                        }
                    }
                }

                ArrayAdapter<String> nameArrayAdapter = new ArrayAdapter<>(SuccessActivity.this, R.layout.spinner_item_design, arrayList);
                spinner.setAdapter(nameArrayAdapter);

                for (int i = 0; i < arrayListName.size(); i++) {
                    nameArray = new String[arrayListName.size()];
                    nameArray = arrayListName.toArray(nameArray);

                    emailArray = new String[arrayListEmail.size()];
                    emailArray = arrayListEmail.toArray(emailArray);
                }
                listAdapterClass = new ListAdapterClass(SuccessActivity.this, R.layout.list_item_design, nameArray, emailArray);
                listView.setAdapter(listAdapterClass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
