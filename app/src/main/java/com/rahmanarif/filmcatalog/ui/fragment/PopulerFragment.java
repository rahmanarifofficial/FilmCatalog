package com.rahmanarif.filmcatalog.ui.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.Result;

import java.util.ArrayList;
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
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Movie> searchMovies = new ArrayList<>();
    private static final String STATE = "state";
    private static final String SEARCH_STATE = "search_state";

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnSearch.setOnClickListener(this);
        if (savedInstanceState == null) {
            loadMovie();
        } else {
            movies = savedInstanceState.getParcelableArrayList(STATE);
            searchMovies = savedInstanceState.getParcelableArrayList(SEARCH_STATE);
            progressBar.setVisibility(View.GONE);
            adapter = new MovieAdapter(movies);
            listPopulerFilm.setLayoutManager(new LinearLayoutManager(getContext()));
            listPopulerFilm.setAdapter(adapter);
        }
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
                    movies.addAll(response.body().getResults());
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    listPopulerFilm.setLayoutManager(new LinearLayoutManager(getContext()));
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
                listPopulerFilm.setLayoutManager(new LinearLayoutManager(getContext()));
                listPopulerFilm.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE, movies);
        outState.putParcelableArrayList(SEARCH_STATE, searchMovies);
        super.onSaveInstanceState(outState);
    }
}
