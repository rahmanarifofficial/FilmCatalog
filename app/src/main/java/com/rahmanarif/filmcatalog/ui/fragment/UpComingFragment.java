package com.rahmanarif.filmcatalog.ui.fragment;


import android.os.Bundle;
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
import com.rahmanarif.filmcatalog.model.ResultMovie;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment {

    private static final String STATE = "statete";
    private RecyclerView listUpcomingFilm;
    private ProgressBar progressBar;

    private MovieAdapter adapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_up_coming, container, false);
        listUpcomingFilm = v.findViewById(R.id.listComingFilm);
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
            adapter = new MovieAdapter(movies);
            progressBar.setVisibility(View.GONE);
            listUpcomingFilm.setLayoutManager(new LinearLayoutManager(getContext()));
            listUpcomingFilm.setAdapter(adapter);
        }
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResultMovie> call = apiService.getMovieUpComing(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<ResultMovie>() {
            @Override
            public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                if (response.body() != null) {
                    movies = response.body().getResults();
                    Log.d(STATE, Integer.toString(movies.size()));
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    listUpcomingFilm.setLayoutManager(new LinearLayoutManager(getContext()));
                    listUpcomingFilm.setAdapter(adapter);
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
