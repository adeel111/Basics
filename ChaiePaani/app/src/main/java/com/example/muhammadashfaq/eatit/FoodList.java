package com.example.muhammadashfaq.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadashfaq.eatit.Common.Common;
import com.example.muhammadashfaq.eatit.Database.Database;
import com.example.muhammadashfaq.eatit.Interface.ItemClickListner;
import com.example.muhammadashfaq.eatit.Model.FoodModel;
import com.example.muhammadashfaq.eatit.ViewHolder.FoodViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference foodlist;

    SpinKitView spinKitView;
    TextView txtVuMainHeading;

    FirebaseRecyclerAdapter<FoodModel,FoodViewHolder> foodAdapter;

    //Firebase search  adapter
    FirebaseRecyclerAdapter<FoodModel,FoodViewHolder> searchAdapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //Favorites
    Database localDB;


    //Facebook Share
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    //Create Target fron Picasso
    Target target=new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto sharePhoto=new SharePhoto.Builder().setBitmap(bitmap).build();
            if(ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content= new SharePhotoContent.Builder().addPhoto(sharePhoto).build();

                shareDialog.show(content);

            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    String categoryID="";
    String categoryName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        txtVuMainHeading=findViewById(R.id.tv_sub_category);
        spinKitView=findViewById(R.id.spin_kit_sub);


        //Firebase init
        firebaseDatabase=FirebaseDatabase.getInstance();
        foodlist=firebaseDatabase.getReference("Foods");
        foodlist.keepSynced(true);

        recyclerView=findViewById(R.id.recycler_food_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Init Facebook
        callbackManager=CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);



        //LocalDb for Favorites
        localDB=new Database(this);

        //Getting category id from intent
        if(getIntent()!=null){
            categoryID=getIntent().getStringExtra("CategoryId");
            categoryName=getIntent().getStringExtra("CategoryName");
            txtVuMainHeading.setText(categoryName);
            if(categoryID != null)
            {

                if(Common.isConnectedtoInternet(getBaseContext())) {
                    loadListFood(categoryID);
                }else{
                    Toast.makeText(FoodList.this, "Check your internet connection !!", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
            materialSearchBar=findViewById(R.id.searchBar);
            materialSearchBar.setHint("Enter Food Name");
            materialSearchBar.setPlaceHolder("Search");
            materialSearchBar.setSpeechMode(false);

            loadSuggested();
            materialSearchBar.setLastSuggestions(suggestList);
            materialSearchBar.addTextChangeListener(new TextWatcher() {
                //When user will enter some text we will change suggestion
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest=new ArrayList<>();
                    for(String search:suggestList){
                        if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                            suggest.add(search);
                        }
                    }
                    materialSearchBar.setLastSuggestions(suggest);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {
                    //When search bar is closed...restore original suggest adapter
                    if(!enabled){
                        recyclerView.setAdapter(foodAdapter);
                    }
                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    //when result finish....show result of search adapter
                    startSearch(text);

                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });

    }
}

    private void startSearch(CharSequence text) {
        searchAdapter=new FirebaseRecyclerAdapter<FoodModel, FoodViewHolder>(
                FoodModel.class,R.layout.food_list_layout,
                FoodViewHolder.class,
                foodlist.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, FoodModel model, int position) {
                //Setting food name and image
                viewHolder.foodName.setText(model.getName());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.placeholder).into(viewHolder.foodImageView);

                final FoodModel local=model;

                viewHolder.setItemClickListnerFood(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Starting food details activity
                        Intent intent=new Intent(FoodList.this,FoodDetail.class);
                        intent.putExtra("foodid",searchAdapter.getRef(position).getKey());//Sending food id to next activity
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);//setting up adappter for recycler search view
    }

    private void loadSuggested() {
        foodlist.orderByChild("menuId").equalTo(categoryID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodModel item = snapshot.getValue(FoodModel.class);
                    suggestList.add(item.getName());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood(String categoryID) {
           /* final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading Sub Menu");
            progressDialog.setCancelable(true);
            progressDialog.show();*/
           spinKitView.setVisibility(View.VISIBLE);

            foodAdapter = new FirebaseRecyclerAdapter<FoodModel, FoodViewHolder>(FoodModel.class,
                    R.layout.food_list_layout,
                    FoodViewHolder.class,
                    foodlist.orderByChild("menuId").equalTo(categoryID)) //like select * from Foods where menuId=...
            {
                @Override
                protected void populateViewHolder(final FoodViewHolder viewHolder, final FoodModel model, final int position) {
                   spinKitView.setVisibility(View.GONE);



                    //Setting food name and image
                    viewHolder.foodName.setText(model.getName());
                    Picasso.get().load(model.getImage()).placeholder(R.drawable.placeholder).into(viewHolder.foodImageView);


                    viewHolder.shareFoodImgVu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Picasso.get().load(model.getImage()).into(target);
                        }
                    });


                    //Add to Favorites
                    if(localDB.isFavorite(foodAdapter.getRef(position).getKey())){
                        viewHolder.foodFavImagView.setImageResource(R.drawable.ic_favorite_black_24dp);
                    }

                    //change status of favorite
                    viewHolder.foodFavImagView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!localDB.isFavorite(foodAdapter.getRef(position).getKey())){
                                localDB.addToFavorites(foodAdapter.getRef(position).getKey());
                                viewHolder.foodFavImagView.setImageResource(R.drawable.ic_favorite_black_24dp);
                                Toast.makeText(FoodList.this, " "+ model.getName()+" added to Favorties", Toast.LENGTH_SHORT).show();

                            }else{
                                localDB.removeFromFavorites(foodAdapter.getRef(position).getKey());
                                viewHolder.foodFavImagView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                Toast.makeText(FoodList.this, " "+ model.getName()+" removed from Favorties", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    final FoodModel local = model;

                    viewHolder.setItemClickListnerFood(new ItemClickListner() {
                        @Override
                        public void onClick(View view, int position, boolean isLongClick) {
                            //Starting food details activity
                            Intent intent = new Intent(FoodList.this, FoodDetail.class);
                            intent.putExtra("foodid", foodAdapter.getRef(position).getKey());//Sending food id to next activity
                            startActivity(intent);
                        }
                    });
                }
            };
            foodAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(foodAdapter);

    }

}
