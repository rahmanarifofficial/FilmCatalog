package com.rahmanarif.filmcatalog.ui.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.adapter.TvShowAdapter;
import com.rahmanarif.filmcatalog.helper.MappingHelper;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.TvShow;

import java.util.ArrayList;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.CONTENT_URI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.TvTable.CONTENT_URI2;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMovieFragment extends Fragment {


    private static final String STATE = "statete";
    private RecyclerView listFavoriteFilm;
    private ProgressBar progressBar;
    private TextView tvNoList;

    private MovieAdapter adapterMovie;
    private ArrayList<Movie> movies = new ArrayList<>();

    private String TAG = "statusstate";
    public FavoriteMovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_movie, container, false);
        listFavoriteFilm = v.findViewById(R.id.listFavoritFilm);
        progressBar = v.findViewById(R.id.progressBar);
        tvNoList = v.findViewById(R.id.no_content_movie_text);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("statusstate", "created");
        super.onActivityCreated(savedInstanceState);
        loadMovie();
    }

    @Override
    public void onResume() {
        Log.d("statusstate", "OK");
        super.onResume();
        loadMovie();
    }

    private void loadMovie() {
        Log.d(TAG, "load movie ok");
        progressBar.setVisibility(View.GONE);
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        Log.d(TAG, "Cursor berikut "+cursor.toString());
        Log.d(TAG, "Jumlah Cursor "+cursor.getCount());
        if (cursor.getCount() > 0) {
            movies = MappingHelper.mapCursorToMovieArrayList(cursor);
            tvNoList.setVisibility(View.GONE);

            adapterMovie = new MovieAdapter(movies);
            listFavoriteFilm.setLayoutManager(new LinearLayoutManager(getContext()));
            listFavoriteFilm.setAdapter(adapterMovie);
        }
    }
}
