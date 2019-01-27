package com.rahmanarif.filmcatalog.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment {

    private RecyclerView listUpcomingFilm;
    private ProgressBar progressBar;

    private MovieAdapter adapter;
    private List<Movie> movies;

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

        loadMovie();
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Result> call = apiService.getMovieUpComing(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                movies = response.body().getResults();
                if (movies != null) {
                    progressBar.setVisibility(View.GONE);
                    adapter = new MovieAdapter(movies);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                    listUpcomingFilm.setLayoutManager(layoutManager);
                    listUpcomingFilm.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}