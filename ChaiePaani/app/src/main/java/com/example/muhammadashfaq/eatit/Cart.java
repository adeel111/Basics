package com.example.muhammadashfaq.eatit;

import android.content.DialogInterface;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Database.Database;
import com.example.muhammadashfaq.eatit.Model.Order;
import com.example.muhammadashfaq.eatit.Model.Request;
import com.example.muhammadashfaq.eatit.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    TextView txtVuToal;
    Button btnPlaceOrder;


    FirebaseDatabase database;
    DatabaseReference requests;
    Request request;

    List<Order> carts=new ArrayList<>();
    CartAdapter cartAdapter;
    String status="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        requests.keepSynced(true);

        //init
        recyclerView=findViewById(R.id.cart_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtVuToal=findViewById(R.id.txt_vu_total);
        btnPlaceOrder=findViewById(R.id.btn_place_order);

        loadListFood();
    }

    private void loadListFood() {
        carts=new  Database(this).getCarts();
        cartAdapter=new CartAdapter(carts,this);
        cartAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartAdapter);

        //Calculate totla price
        int total=0;
        for (Order order:carts){
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuanitity()));

        }

        Locale locale=new Locale("en","US");
        NumberFormat format=NumberFormat.getCurrencyInstance(locale);
        txtVuToal.setText(format.format(total));

        btnPlaceOrder=findViewById(R.id.btn_place_order);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(carts.size()==0)
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                else
                showAlertDailog();
            }
        });

    }

    private void showAlertDailog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(Cart.this);
        builder.setTitle("One more Step!");
        builder.setMessage("Enter you Adress");

        final EditText editText=new EditText(Cart.this);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(layoutParams);
        builder.setView(editText);
        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                request=new Request(Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editText.getText().toString(),
                        txtVuToal.getText().toString(),
                        carts,
                        status);

                //Submitting to firbase
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //Clear Cart
                new Database(getBaseContext()).clearCart();
                customToast(true);
                finish();
            }


        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void customToast(boolean b)
    {
        View customToastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup)findViewById(R.id.custom_toast));
        TextView textViewToast=customToastView.findViewById(R.id.custom_toast_txt_vu);
        if(b){
            textViewToast.setText("Thank you.. Order placed");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();
        }else
        {
            textViewToast.setText("No");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();

        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
            return true;

    }

    private void deleteCart(int position) {
        carts.remove(position);
        new Database(this).clearCart();

        for (Order item:carts ){
            new Database(this).addtoCart(item);
            loadListFood();
        }
    }
}
