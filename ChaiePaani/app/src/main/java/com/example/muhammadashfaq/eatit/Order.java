package com.example.muhammadashfaq.eatit;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.Model.Request;
import com.example.muhammadashfaq.eatit.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Order extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> recyclerAdapter;


    FirebaseDatabase database;
    DatabaseReference requests,users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        requests.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_list_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


       if (getIntent() != null)
           loadOrder(Common.currentUser.getPhone());
        else
            loadOrder(getIntent().getStringExtra("userPhone"));

    }


    private void loadOrder(String phone) {



        if (Common.isConnectedtoInternet(getBaseContext())) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Orders");
            progressDialog.setCancelable(true);
            progressDialog.show();
            recyclerAdapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(Request.class, R.layout.order_layout, OrderViewHolder.class,
                    requests.orderByChild("phone").equalTo(phone)) {
                @Override
                protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, int position) {

                    if(model==null){
                        progressDialog.dismiss();
                    }else{
                        progressDialog.dismiss();
                        viewHolder.txtVuOrderid.setText(recyclerAdapter.getRef(position).getKey());
                        viewHolder.txtVuOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                        viewHolder.txtVuOrderAdress.setText(model.getAdress());
                        viewHolder.txtVuPrice.setText(model.getTotal());
                        viewHolder.txtVuOrderPhone.setText(Common.currentUser.getPhone());

                        viewHolder.setItemClickListner(new ItemClickListner() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Toast.makeText(Order.this, model.getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            };

            recyclerAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(recyclerAdapter);
        }else{
            Snackbar.make(getWindow().getDecorView().getRootView(),"Check internet connection !!!",Snackbar.LENGTH_LONG).show();

        }
    }

    private String convertCodeToStatus(String status) {
        if(status.equals("0")){
            return "Placed";
        }else if(status.equals("1")){
            return "On your way";

        }else{
            return "Shipped";
        }
    }
}
