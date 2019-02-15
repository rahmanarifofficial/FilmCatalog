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
import android.widget.ProgressBar;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    private static final String STATE = "state";
    private RecyclerView listPlayingFilm;
    private ProgressBar progressBar;

    private MovieAdapter adapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_now_playing, container, false);
        listPlayingFilm = v.findViewById(R.id.listPlayingFilm);
        progressBar = v.findViewById(R.id.progressBar);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            loadMovie();
        } else {
            movies = savedInstanceState.getParcelableArrayList(STATE);
            progressBar.setVisibility(View.GONE);
            adapter = new MovieAdapter(movies);
            listPlayingFilm.setLayoutManager(new LinearLayoutManager(getContext()));
            listPlayingFilm.setAdapter(adapter);
        }
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Result> call = apiService.getMovieNowPlaying(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.body() != null) {
                    movies = response.body().getResults();
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    listPlayingFilm.setLayoutManager(new LinearLayoutManager(getContext()));
                    listPlayingFilm.setAdapter(adapter);
                }
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
        super.onSaveInstanceState(outState);
    }
}
