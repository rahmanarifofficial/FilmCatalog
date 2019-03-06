package com.rahmanarif.filmcatalog.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.adapter.TvShowAdapter;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.ResultMovie;
import com.rahmanarif.filmcatalog.model.ResultTvShow;
import com.rahmanarif.filmcatalog.model.TvShow;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {

    public static final String EXTRA_QUERY_SEARCH = "EXTRA_QUERY_SEARCH";

    private RecyclerView listFilm, listTV;
    private MovieAdapter movieAdapter;
    private TvShowAdapter tvShowAdapter;

    private ArrayList<Movie> searchMovies = new ArrayList<>();
    private ArrayList<TvShow> searchTvShows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        listFilm = findViewById(R.id.listResultFilm);
        listTV = findViewById(R.id.listResultTV);
        String query = getIntent().getStringExtra(EXTRA_QUERY_SEARCH);
        if (!query.isEmpty()) {
            searchMovie(query);
            searchTv(query);
        }
        getSupportActionBar().setTitle("Hasil Pencarian " + query);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    private void searchMovie(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResultMovie> callMovie = apiService.getSearchMovie(BuildConfig.TSDB_API_KEY, query);

        callMovie.enqueue(new Callback<ResultMovie>() {
            @Override
            public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                searchMovies.addAll(response.body().getResults());

                movieAdapter = new MovieAdapter(searchMovies);
                listFilm.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                listFilm.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(Call<ResultMovie> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void searchTv(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResultTvShow> callTvShow = apiService.getSearchTvShow(BuildConfig.TSDB_API_KEY, query);
        callTvShow.enqueue(new Callback<ResultTvShow>() {
            @Override
            public void onResponse(Call<ResultTvShow> call, Response<ResultTvShow> response) {
                searchTvShows.addAll(response.body().getResults());

                tvShowAdapter = new TvShowAdapter(searchTvShows);
                listTV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                listTV.setAdapter(tvShowAdapter);
            }

            @Override
            public void onFailure(Call<ResultTvShow> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
