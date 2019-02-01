package com.rahmanarif.filmcatalog.ui.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.MainActivity;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopulerFragment extends Fragment implements View.OnClickListener {

    private ImageButton btnSearch;
    private EditText edtSearch;
    private RecyclerView listPopulerFilm;
    private ProgressBar progressBar;

    private MovieAdapter adapter;
    private List<Movie> movies;
    private List<Movie> searchMovies;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_populer, container, false);
        btnSearch = v.findViewById(R.id.btnSearch);
        edtSearch = v.findViewById(R.id.edtSearch);
        listPopulerFilm = v.findViewById(R.id.listPopulerFilm);
        progressBar = v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadMovie();
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSearch) {
            String query = edtSearch.getText().toString();
            if (!query.isEmpty()) {
                searchMovie(query);
            } else {
                Toast.makeText(getContext(), "Field harus diisi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Result> call = apiService.getMoviePopuler(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    movies = response.body().getResults();
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    listPopulerFilm.setLayoutManager(layoutManager);
                    listPopulerFilm.setAdapter(adapter);
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                listPopulerFilm.setLayoutManager(layoutManager);
                listPopulerFilm.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
