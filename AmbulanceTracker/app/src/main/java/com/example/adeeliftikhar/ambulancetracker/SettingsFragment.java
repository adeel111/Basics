package com.example.adeeliftikhar.ambulancetracker;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.Internet.CheckInternetConnectivity;
import com.example.adeeliftikhar.ambulancetracker.SessionsManager.LoginSessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    Button buttonDeleteAccount;
    FirebaseAuth mAuth;
    String currentUser;
    DatabaseReference dbRefData;
    DatabaseReference dbRefUser;
    StorageReference storageRef;
    ProgressDialog progressDialog;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        buttonDeleteAccount = view.findViewById(R.id.button_delete_account);

//        Database Initialization...
        mAuth = FirebaseAuth.getInstance();
        currentUser = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        dbRefData = FirebaseDatabase.getInstance().getReference().child("HelperHistory").child(currentUser);
        dbRefUser = FirebaseDatabase.getInstance().getReference().child("PhoneNumberUsers").child(currentUser);
        storageRef = FirebaseStorage.getInstance().getReference().child("IncidentImage").child(currentUser);
        buttonDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlertBox();
            }
        });
        return view;
    }

    private void deleteAlertBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete Account");
        builder.setMessage("Do you want to Delete your Account?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!CheckInternetConnectivity.isConnected(getContext())) {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                } else {
                    deleteAccount();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void deleteAccount() {
        showProgressDialog();
        final String phoneNumber = Objects.requireNonNull(mAuth.getCurrentUser()).getPhoneNumber();
        final String phoneNumber2 = "12345678901";

        Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).unlink(PhoneAuthProvider.PROVIDER_ID).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    assert phoneNumber != null;
                    PhoneAuthCredential credentials = PhoneAuthProvider.getCredential(phoneNumber, phoneNumber2);
                    FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                            deleteAllData();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failure ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failure ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Deleting Account...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void deleteAllData() {
        dbRefData.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dbRefUser.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                clearLoginSession();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failure Inside ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Failure Outside ==> " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearLoginSession() {
        LoginSessionManager loginSessionManager = new LoginSessionManager(getContext());
        loginSessionManager.loginTheUser(false, "", "");
        progressDialog.dismiss();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
