package com.rahmanarif.favoritelist.api;

import com.rahmanarif.favoritelist.model.Movie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/{movie_id}")
    Call<Movie> getDetailsMovie(@Path("movie_id") String movie_id, @Query("api_key") String api_key);
}
