package com.rahmanarif.filmcatalog.api;

import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.ResultMovie;
import com.rahmanarif.filmcatalog.model.ResultTvShow;
import com.rahmanarif.filmcatalog.model.TvShow;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie/popular")
    Call<ResultMovie> getMoviePopuler(@Query("api_key") String api_key);

    @GET("movie/now_playing")
    Call<ResultMovie> getMovieNowPlaying(@Query("api_key") String api_key);

    @GET("movie/upcoming")
    Call<ResultMovie> getMovieUpComing(@Query("api_key") String api_key);

    @GET("discover/tv")
    Call<ResultTvShow> getDiscoverTvShow(@Query("api_key") String api_key);

    @GET("search/movie")
    Call<ResultMovie> getSearchMovie(@Query("api_key") String api_key, @Query("query") String query);

    @GET("search/tv")
    Call<ResultTvShow> getSearchTvShow(@Query("api_key") String api_key, @Query("query") String query);

    @GET("movie/{movie_id}")
    Call<Movie> getDetailsMovie(@Path("movie_id") String movie_id, @Query("api_key") String api_key);

    @GET("tv/{tv_id}")
    Call<TvShow> getDetailsTv(@Path("tv_id") String tv_id, @Query("api_key") String api_key);


}
