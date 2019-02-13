package com.example.aydil.spinnerwithadapter.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aydil.spinnerwithadapter.MainActivity;
import com.example.aydil.spinnerwithadapter.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    int[] flagPics;
    String[] countryNames;
    Context context;
    Resources resources;
    LayoutInflater layoutInflater;

    public CustomAdapter(MainActivity context, int[] flagPics) {
        this.flagPics = flagPics;
        resources = context.getResources();
        this.context = context;
        layoutInflater = (LayoutInflater.from(context));
        countryNames = resources.getStringArray(R.array.spinner_data);
    }

    public CustomAdapter(MainActivity mainActivity) {
    }

    @Override
    public int getCount() {
        return flagPics.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.layout_for_adapter, null);
        ImageView imageView = convertView.findViewById(R.id.image_view);
        final TextView textView = convertView.findViewById(R.id.text_view);

        imageView.setImageResource(flagPics[position]);
        textView.setText(countryNames[position]);

//        For un_necessary Navigation
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, textView.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }
}
