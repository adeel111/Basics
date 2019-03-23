package com.example.adeeliftikhar.jsonparsingusingretrofit.rest;

import com.example.adeeliftikhar.jsonparsingusingretrofit.model.MovieModel;
import com.example.adeeliftikhar.jsonparsingusingretrofit.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
//    @GET annotation used to describe we are going to get something.
//    "movie/top_rated" describes where we are going to get something.
//    movie/top_rated ==> end point.

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieModel> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
}