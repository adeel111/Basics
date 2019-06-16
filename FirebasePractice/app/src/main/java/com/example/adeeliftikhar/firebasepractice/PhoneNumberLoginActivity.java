package com.example.adeeliftikhar.firebasepractice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class PhoneNumberLoginActivity extends AppCompatActivity {

    Spinner spinnerCountries;
    EditText editTextNumber;
    String country, number;

    FirebaseAuth mAuth;
    DatabaseReference dbRef;
    String currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_login);

//        It will initialize all the Widgets...
        initializer();
//        Setting All Countries To Spinner Programmatically...
        settingValueToSpinner();
    }

    private void initializer() {
        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("PhoneNumberUsers").child(currentId);

        spinnerCountries = findViewById(R.id.spinner_countries);
        editTextNumber = findViewById(R.id.edit_text_number);
    }

    private void settingValueToSpinner() {
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for (Locale loc : locale) {
            country = loc.getDisplayCountry();
            if (country.length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        spinnerCountries.setSelection(5);
        spinnerCountries.setAdapter(adapter);
    }

    public void buttonNumberLogin(View view) {
        country = spinnerCountries.getSelectedItem().toString().trim();
        number = editTextNumber.getText().toString().trim();

        Boolean response = validateData(country, number);
        if (response.equals(true)) {
            Toast.makeText(this, "Please wait.", Toast.LENGTH_SHORT).show();
            saveCredentialsToDB();
        }
    }

    private Boolean validateData(String country, String number) {
        if (!country.equals("Pakistan")) {
            Toast.makeText(this, "Country should be Pakistan", Toast.LENGTH_SHORT).show();
            return false;
        } else if (number.isEmpty()) {
            editTextNumber.setError("Number is Required");
            editTextNumber.requestFocus();
            return false;
        } else if (number.length() != 11) {
            Toast.makeText(this, "Number Should Contain 11 Digits", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveCredentialsToDB() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("country", country);
        hashMap.put("number", number);
        dbRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(PhoneNumberLoginActivity.this, VarifyPhoneNumberActivity.class);
                    intent.putExtra("name", country);
                    intent.putExtra("number", number);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PhoneNumberLoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}