package com.rahmanarif.filmcatalog.api;

import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular")
    Call<Result> getMoviePopuler(@Query("api_key") String api_key);

    @GET("movie/now_playing")
    Call<Result> getMovieNowPlaying(@Query("api_key") String api_key);

    @GET("movie/upcoming")
    Call<Result> getMovieUpComing(@Query("api_key") String api_key);

    @GET("search/movie")
    Call<Result> getSearchMovie(@Query("api_key") String api_key, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<Movie> getDetailsMovie(@Path("movie_id") String movie_id, @Query("api_key") String api_key);
}
