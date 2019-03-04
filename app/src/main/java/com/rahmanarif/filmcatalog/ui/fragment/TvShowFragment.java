package com.rahmanarif.filmcatalog.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment {

    private static final String STATE = "state";

    private RecyclerView listTvShow;
    private ProgressBar progressBar;

    private TvShowAdapter adapter;
    private ArrayList<TvShow> tvShows = new ArrayList<>();

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tv_show, container, false);
        listTvShow = v.findViewById(R.id.listTvShow);
        progressBar = v.findViewById(R.id.progressBar);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            loadMovie();
        } else {
            tvShows = savedInstanceState.getParcelableArrayList(STATE);
            progressBar.setVisibility(View.GONE);
            adapter = new TvShowAdapter(tvShows);
            listTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
            listTvShow.setAdapter(adapter);
        }
    }

    private void loadMovie() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResultTvShow> call = apiService.getDiscoverTvShow(BuildConfig.TSDB_API_KEY);
        call.enqueue(new Callback<ResultTvShow>() {
            @Override
            public void onResponse(Call<ResultTvShow> call, Response<ResultTvShow> response) {
                if (response.body() != null) {
                    tvShows = response.body().getResults();
                    progressBar.setVisibility(View.GONE);
                    adapter = new TvShowAdapter(tvShows);
                    listTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
                    listTvShow.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResultTvShow> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE, tvShows);
        super.onSaveInstanceState(outState);
    }
}
