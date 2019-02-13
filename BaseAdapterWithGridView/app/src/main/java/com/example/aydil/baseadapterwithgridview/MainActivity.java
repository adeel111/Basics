package com.example.aydil.baseadapterwithgridview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    int [] imageArray = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,
            R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten,
            R.drawable.dep,R.drawable.yep,R.drawable.one,R.drawable.two,R.drawable.three,
            R.drawable.four,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,
            R.drawable.ten,R.drawable.dep,R.drawable.yep};
    String [] text = {"Private Image","Private Image","Private Image","Private Image","Private Image",
            "Private Image","Private Image","Private Image","Private Image","Private Image","Private Image",
            "Private Image","Private Image","Private Image","Private Image","Private Image",
            "Private Image","Private Image","Private Image","Private Image","Private Image","Private Image"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = (GridView) findViewById(R.id.grid_view);
        CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,R.layout.layout_for_adapter,imageArray,text);
        gridView.setAdapter(customAdapter);
    }
}
