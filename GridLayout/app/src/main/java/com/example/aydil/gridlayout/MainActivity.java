package com.example.aydil.gridlayout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridLayout = (GridLayout) findViewById(R.id.grid_layout);

//        Set Event
//        setSingleEvent(gridLayout);
//        Set Event
        setToggleEvent(gridLayout);
    }

    private void setToggleEvent(GridLayout gridLayout) {
//        Loop for all child items of grid layout
        for (int i = 0; i<gridLayout.getChildCount(); i++)
        {
//            Hence all child's are card view...
            final CardView cardView = (CardView) gridLayout.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1)
                    {
//                        Change Background Color...
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFF00"));
                    }
                    else
                    {
//                        Change Background Color...
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    }
                }
            });
    }

//    private void setSingleEvent(GridLayout gridLayout) {
////        Loop for all child items of grid layout
//        for (int i = 0; i<gridLayout.getChildCount(); i++)
//        {
////            Hence all child's are card view...
//            CardView cardView = (CardView) gridLayout.getChildAt(i);
//            final  int i1 = i;
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(MainActivity.this, "Clicked at index " + i1, Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }
}