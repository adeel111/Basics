package com.example.muhammadashfaq.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Model.AddResturantModel;
import com.example.muhammadashfaq.eatit.ViewHolder.RecntlyAddedResViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecentlyAddedResActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase db;
    DatabaseReference reference;

    FirebaseRecyclerAdapter<AddResturantModel, RecntlyAddedResViewHolder> recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recently_added_res);
        //Firebase init
        db = FirebaseDatabase.getInstance();
        reference = db.getReference("Resturants");

        //Init
        recyclerView = findViewById(R.id.recycler_list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();

    }

    private void loadOrders() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Resturants Requests");
        progressDialog.show();

        recyclerAdapter = new FirebaseRecyclerAdapter<AddResturantModel, RecntlyAddedResViewHolder>(AddResturantModel.class, R.layout.add_res_request_layout, RecntlyAddedResViewHolder.class,
                reference) {
            @Override
            protected void populateViewHolder(RecntlyAddedResViewHolder viewHolder, final AddResturantModel model, int position) {
                progressDialog.dismiss();
                viewHolder.txtVuOrderStatus.setText(model.getName());
                viewHolder.txtVuOrderPhone.setText(model.getLatitude());
                viewHolder.txtVuOrderAdress.setText(model.getLongitude());
                viewHolder.txtVuOrderOriginalStatus.setText(Common.convertResturatCode(model.getStatus()));


                viewHolder.txtVuOrderStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(RecentlyAddedResActivity.this, AddResturantMapActivity.class);
                        intent.putExtra("name", model.getName());
                        intent.putExtra("latitude", model.getLatitude());
                        intent.putExtra("longitude", model.getLongitude());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(recyclerAdapter);
    }
}
