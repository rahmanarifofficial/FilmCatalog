package com.rahmanarif.filmcatalog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnSearch;
    private EditText edtSearch;
    private RecyclerView listFilm;
    private ProgressBar progressBar;

    private MovieAdapter adapter;
    private List<Movie> movies;
    private List<Movie> searchMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearch);
        listFilm = findViewById(R.id.listFilm);
        progressBar = findViewById(R.id.progressBar);

        loadMovie();
        btnSearch.setOnClickListener(this);
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Result> call = apiService.getMoviePopuler(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                movies = response.body().getResults();
                if (movies != null) {
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    listFilm.setLayoutManager(layoutManager);
                    listFilm.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void searchMovie(String query) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Result> call = apiService.getSearchMovie(BuildConfig.TSDB_API_KEY, query);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                searchMovies = response.body().getResults();

                    adapter = new MovieAdapter(searchMovies);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    listFilm.setLayoutManager(layoutManager);
                    listFilm.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            String query = edtSearch.getText().toString();
            if (!query.isEmpty()) {
                searchMovie(query);
            } else {
                Toast.makeText(this, "Field harus diisi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
