package com.example.adeeliftikhar.firebasepractice.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.adeeliftikhar.firebasepractice.R;
import com.example.adeeliftikhar.firebasepractice.SuccessActivity;

import java.util.ArrayList;

/**
 * Created by Adeel on 9/8/2017.
 */

public class ListAdapterClass extends ArrayAdapter {
    Context context;
    int resource;
    String[] textViewName;
    String[] textViewMail;
    LayoutInflater myLayoutInflater;

    public ListAdapterClass(@NonNull Context context, @LayoutRes int resource, String[] textViewName, String[] textViewMail) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.textViewName = textViewName;
        this.textViewMail = textViewMail;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return textViewName.length;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = myLayoutInflater.inflate(R.layout.list_item_design, null);
        TextView name = convertView.findViewById(R.id.text_view_name);
        TextView email = convertView.findViewById(R.id.text_view_email);

        name.setText(textViewName[position]);
        email.setText(textViewMail[position]);
        return convertView;
    }
}
