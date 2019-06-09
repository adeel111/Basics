package com.example.muhammadashfaq.eatit;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Database.Database;
import com.example.muhammadashfaq.eatit.Model.FoodModel;
import com.example.muhammadashfaq.eatit.Model.Order;
import com.example.muhammadashfaq.eatit.Model.Rating;
import com.example.muhammadashfaq.eatit.SessionManager.SessionManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Queue;
import java.util.jar.Attributes;

public class FoodDetail extends AppCompatActivity  {

    TextView txtVuFoodName,txtVuFoodDescriotion,txtVuPrice;
    ImageView imgVuFood;

    CollapsingToolbarLayout collapsingToolbarLayout;
    android.support.design.widget.FloatingActionButton fabCart,fabRate;
    RatingBar ratingBar;
    ElegantNumberButton numberButton;

    String foodid="";

    FoodModel currentFood;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference RatingRef;
    DatabaseReference userRef;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        txtVuFoodName=findViewById(R.id.txt_vu_food_name);
        txtVuPrice=findViewById(R.id.txt_vu_price);
        ratingBar=findViewById(R.id.rating_bar);
        txtVuFoodDescriotion=findViewById(R.id.txt_vu_food_description);
        fabCart=findViewById(R.id.fab_cart);
        fabRate=findViewById(R.id.fab_rating);
        imgVuFood=findViewById(R.id.image_vu_food);
        numberButton=findViewById(R.id.number_elegant);


        //Coloapsing toolbar customization
        collapsingToolbarLayout=findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);

        //Firebase Init
        firebaseDatabase=FirebaseDatabase.getInstance();
        foods=firebaseDatabase.getReference("Foods");
        RatingRef=firebaseDatabase.getReference("Ratings");
        userRef=firebaseDatabase.getReference("User");
        foods.keepSynced(true);

        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database database=new Database(getBaseContext());
                database.addtoCart(new Order(
                        foodid,
                        currentFood.getName(),
                        numberButton.getNumber(),
                        currentFood.getPrice(),
                        currentFood.getDiscount()));

                customToast(true);
            }
        });
        fabRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDailog();
            }
        });
        //Get food id from intent
        if(getIntent() != null)
            foodid = getIntent().getStringExtra("foodid");

        if(Common.isConnectedtoInternet(getBaseContext())) {
            getDetailedFood(foodid);
        }else{
            Toast.makeText(this, "Check internet connection !", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void getRatingFood(String foodid) {
        Query query=RatingRef.orderByChild("fooodid").equalTo(foodid);
        query.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postsnapshot:dataSnapshot.getChildren()){
                    Rating item=postsnapshot.getValue(Rating.class);
                    sum= Integer.parseInt(sum+item.getRateValue());
                    count++;

                }
                if(count!=0){
                    float average=sum/count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDailog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(1)
                .setTitle("Rate this food")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("This food is so yummy !")
                .setStarColor(R.color.colorAccent)
                .setNoteDescriptionTextColor(R.color.colorPrimary)
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDailogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .create(FoodDetail.this)
                .show();
    }

    private void getDetailedFood(String foodid) {

        //Query
        foods.child(foodid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               currentFood=dataSnapshot.getValue(FoodModel.class);

                //Setting up image
                Picasso.get().load(currentFood.getImage()).placeholder(R.drawable.placeholder).into(imgVuFood);

                collapsingToolbarLayout.setTitle(currentFood.getName());

                txtVuPrice.setText(currentFood.getPrice());

                txtVuFoodName.setText(currentFood.getName());

                txtVuFoodDescriotion.setText(currentFood.getDescription());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void customToast(boolean b)
    {
        View customToastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup)findViewById(R.id.custom_toast));
        TextView textViewToast=customToastView.findViewById(R.id.custom_toast_txt_vu);
        if(b){
            textViewToast.setText("Added to Cart");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();
        }else
        {
            textViewToast.setText("Not Added to Cart");
            Toast toast=new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER,0,0);
            toast.setView(customToastView);
            toast.show();

        }
    }

}
