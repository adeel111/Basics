package com.example.aydil.fragmentswithlistviews.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aydil.fragmentswithlistviews.R;

import java.util.ArrayList;
class SingleRow1 {
    int image;
    public SingleRow1(int image){
        this.image = image;
    }
}
public class GridViewBaseAdapter extends BaseAdapter {

    ArrayList <SingleRow1> arrayList;
    Context context;
    LayoutInflater myLayoutInflater;

    public GridViewBaseAdapter(Context context){
        arrayList = new ArrayList<SingleRow1>();
        this.context = context;
        myLayoutInflater = LayoutInflater.from(context);
        int [] imageArray = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,
                R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten,
                R.drawable.dep,R.drawable.yep,R.drawable.one,R.drawable.two,R.drawable.three,
                R.drawable.four,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,
                R.drawable.ten,R.drawable.dep,R.drawable.yep,R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,
                R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten,
                R.drawable.dep,R.drawable.yep,R.drawable.one,R.drawable.two,R.drawable.three,
                R.drawable.four,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,
                R.drawable.ten,R.drawable.dep,R.drawable.yep};
        for (int i = 0; i<imageArray.length; i++){
            arrayList.add(new SingleRow1(imageArray[i]));
        }
    }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        convertView = myLayoutInflater.inflate(R.layout.grid_view_adapter_layout,null);
        ImageView imageView = convertView.findViewById(R.id.image_view);

        SingleRow1 temp1 = arrayList.get(position);
        imageView.setImageResource(temp1.image);
            return convertView;
        }
    }