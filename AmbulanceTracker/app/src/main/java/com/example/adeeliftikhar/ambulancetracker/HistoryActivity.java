package com.example.adeeliftikhar.ambulancetracker;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adeeliftikhar.ambulancetracker.Models.HelperHistoryModel;
import com.example.adeeliftikhar.ambulancetracker.ViewHolders.HistoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerViewHistory;

    FirebaseAuth mAuth;
    String currentUser;
    private DatabaseReference dbRef;
    private StorageReference storageRef;

    SpinKitView spinKitView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Your Help History");

        spinKitView = findViewById(R.id.spin_kit_view_history);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference().child("HelperHistory").child(currentUser);
        storageRef = FirebaseStorage.getInstance().getReference().child("IncidentImage").child(currentUser);
        dbRef.keepSynced(true);

        recyclerViewHistory = findViewById(R.id.recycler_view_history);
        recyclerViewHistory.setHasFixedSize(true);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
//        DividerItemDecoration divider = new
//                DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.line_divider));
//        recyclerViewHistory.addItemDecoration(divider);

        int[] ATTRS = new int[]{android.R.attr.listDivider};

        TypedArray a = this.obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(HistoryActivity.this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewHistory.addItemDecoration(itemDecoration);

        loadDataFromDB();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadDataFromDB() {
        FirebaseRecyclerAdapter<HelperHistoryModel, HistoryViewHolder> adapter = new
                FirebaseRecyclerAdapter<HelperHistoryModel, HistoryViewHolder>
                        (HelperHistoryModel.class,
                                R.layout.recycler_view_history_design,
                                HistoryViewHolder.class,
                                dbRef) {


                    @Override
                    protected void populateViewHolder(HistoryViewHolder viewHolder, HelperHistoryModel model, int position) {
                        spinKitView.setVisibility(View.GONE);

                        viewHolder.setTextViewAmbulanceNumber(model.getAmbulance());
                        viewHolder.setDateTime(model.getDate_time());

                        final String itemKey = getRef(position).getKey();

                        ImageView navArrow = viewHolder.itemView.findViewById(R.id.nav_arrow);
                        navArrow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(HistoryActivity.this, HelpDetailActivity.class);
                                intent.putExtra("itemKey", itemKey);
                                startActivity(intent);
                            }
                        });
                    }
                };
        recyclerViewHistory.setAdapter(adapter);
    }
}
