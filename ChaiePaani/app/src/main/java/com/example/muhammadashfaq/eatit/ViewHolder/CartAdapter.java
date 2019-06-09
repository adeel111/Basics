package com.example.muhammadashfaq.eatit.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.Model.Order;
import com.example.muhammadashfaq.eatit.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public  TextView txtVuCartItemName,txtVuCartItemPrice;
    public ImageView cart_item_count;

    private ItemClickListner itemClickListner;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtVuCartItemName=itemView.findViewById(R.id.cart_item_name);
        txtVuCartItemPrice=itemView.findViewById(R.id.cart_item_price);
        cart_item_count=itemView.findViewById(R.id.cart_item_image_count);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose Action:");
        menu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData=new ArrayList<>();
    Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable textDrawable=  TextDrawable.builder().buildRound(""+listData.get(position).getQuanitity(), Color.RED);
        holder.cart_item_count.setImageDrawable(textDrawable);

        Locale locale=new Locale("en","US");
        NumberFormat format=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getQuanitity()));
        holder.txtVuCartItemPrice.setText(format.format(price));
        holder.txtVuCartItemName.setText(listData.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
