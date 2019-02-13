package com.example.aydil.baseadapterwithgridview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

class CustomAdapter extends BaseAdapter {
    Context context;
    int[] pics;
    String[] text;
    LayoutInflater layoutInflater;

    public CustomAdapter(Context context, int layout_for_adapter, int[] pics, String[] text) {
        this.context = context;
        this.pics = pics;
        this.text = text;
        layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return pics.length;
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
        ImageView icon = convertView.findViewById(R.id.image_view);
        TextView textView = convertView.findViewById(R.id.text_view);
        icon.setImageResource(pics[position]);
        textView.setText(text[position]);
        return convertView;
    }
}
