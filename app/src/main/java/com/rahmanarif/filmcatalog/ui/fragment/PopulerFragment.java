package com.rahmanarif.filmcatalog.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.rahmanarif.filmcatalog.model.ResultMovie;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopulerFragment extends Fragment {

    private RecyclerView listPopulerFilm;
    private ProgressBar progressBar;

    private MovieAdapter adapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    private static final String STATE = "state";
    private static final String SEARCH_STATE = "search_state";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_populer, container, false);
        listPopulerFilm = v.findViewById(R.id.listPopulerFilm);
        progressBar = v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadMovie();
        } else {
            movies = savedInstanceState.getParcelableArrayList(STATE);
            progressBar.setVisibility(View.GONE);
            adapter = new MovieAdapter(movies);
            listPopulerFilm.setLayoutManager(new LinearLayoutManager(getContext()));
            listPopulerFilm.setAdapter(adapter);
        }
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResultMovie> call = apiService.getMoviePopuler(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<ResultMovie>() {
            @Override
            public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                if (response.body() != null) {
                    movies.addAll(response.body().getResults());
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    listPopulerFilm.setLayoutManager(new LinearLayoutManager(getContext()));
                    listPopulerFilm.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResultMovie> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE, movies);
        super.onSaveInstanceState(outState);
    }
}
