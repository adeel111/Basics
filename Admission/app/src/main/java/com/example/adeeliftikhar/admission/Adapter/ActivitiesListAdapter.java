package com.example.adeeliftikhar.admission.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeeliftikhar.admission.R;

public class ActivitiesListAdapter extends BaseAdapter {
    Context context;
    int resource;
    int[] activitiesImages;
    String[] activitiesName;
    String[] activitiesText;
    LayoutInflater myLayoutInflater;


    public ActivitiesListAdapter(Context context, int resource, int[] activitiesImages, String[] activitiesName, String[] activitiesText) {
//        super(context, resource);
        this.context = context;
        this.resource = resource;
        this.activitiesImages = activitiesImages;
        this.activitiesName = activitiesName;
        this.activitiesText = activitiesText;
        myLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return activitiesName.length;
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
        convertView = myLayoutInflater.inflate(R.layout.recycler_view_activity_design, null);
        ImageView imageViewActivities = convertView.findViewById(R.id.activity_image);
        TextView textViewActivitiesName = convertView.findViewById(R.id.activity_name);
        TextView textViewActivitiesText = convertView.findViewById(R.id.activity_description);

        imageViewActivities.setImageResource(activitiesImages[position]);
        textViewActivitiesName.setText(activitiesName[position]);
        textViewActivitiesText.setText(activitiesText[position]);

        return convertView;
    }
}
