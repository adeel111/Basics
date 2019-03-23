package com.example.adeeliftikhar.jsonparsingusingretrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.adeeliftikhar.jsonparsingusingretrofit.adapter.MoviesAdapter;
import com.example.adeeliftikhar.jsonparsingusingretrofit.model.MovieModel;
import com.example.adeeliftikhar.jsonparsingusingretrofit.model.MovieResponse;
import com.example.adeeliftikhar.jsonparsingusingretrofit.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    ProgressDialog progressDialog;

    // TODO - insert your themoviedb.org API KEY here
    private final static String API_KEY = "44ddfe9ed77b797d418f1fdf610fcf79";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        final RecyclerView recyclerView = findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Retrofit Library...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//        Implementing Interface using Retrofit obj (retrofit).
        ApiInterface apiService = retrofit.create(ApiInterface.class);

//        Now make request to server by using Call class...
        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);

//        Now we will check the response by server...
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if(!response.isSuccessful()){
                    String error = response.errorBody().toString();
                    Toast.makeText(MainActivity.this, "Response is not Successful" + error, Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(MainActivity.this, "Successful Response", Toast.LENGTH_SHORT).show();
                List<MovieModel> movies = response.body().getResultsList();
                progressDialog.dismiss();
                MoviesAdapter moviesAdapter = new MoviesAdapter(movies, R.layout.list_item_movie, getApplicationContext());
                recyclerView.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
