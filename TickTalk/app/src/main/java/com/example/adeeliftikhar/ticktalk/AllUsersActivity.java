package com.example.adeeliftikhar.ticktalk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.adeeliftikhar.ticktalk.Model.UserModel;
import com.example.adeeliftikhar.ticktalk.ViewHolder.UsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class AllUsersActivity extends AppCompatActivity {
    RecyclerView recyclerViewAllUsers;
    DatabaseReference dbReference;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        recyclerViewAllUsers = findViewById(R.id.recycler_view_all_users);
        recyclerViewAllUsers.setHasFixedSize(true);
        recyclerViewAllUsers.setLayoutManager(new LinearLayoutManager(AllUsersActivity.this));

        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");
        dbReference.keepSynced(true);
    }
//    End of onCreate...

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(AllUsersActivity.this);
        progressDialog.setTitle("Loading Data");
        progressDialog.setMessage("Wait Till it Completed...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
//        For this you have to create two other classes, One is viewHolder ( to display data) and second
//        model class ( refers to name of nodes from where you are fetching data ).

        FirebaseRecyclerAdapter<UserModel, UsersViewHolder> adapter = new
                FirebaseRecyclerAdapter<UserModel, UsersViewHolder>
                        (UserModel.class,
                                R.layout.design_for_recycler_view,
                                UsersViewHolder.class,
                                dbReference) {
                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, final UserModel model, int position) {
                        progressDialog.dismiss();
//                        viewHolder.allUserName.setText(model.getName());
                        viewHolder.setName(model.getName());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setThumbImage(model.getThumb_image());

//                        Get Id or Key of user on Recycler Clicked Item.
//                        getRef() ==> Will Get DatabaseReference then we will get the current user key or id.
                        final String userKey = getRef(position).getKey();
                        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AllUsersActivity.this, UsersProfileActivity.class);
                                intent.putExtra("userKey", userKey);
                                startActivity(intent);
                            }
                        });
                    }
                };
        recyclerViewAllUsers.setAdapter(adapter);
    }
}
