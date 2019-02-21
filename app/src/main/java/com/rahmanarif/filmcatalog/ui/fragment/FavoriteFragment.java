package com.rahmanarif.filmcatalog.ui.fragment;

import android.database.Cursor;
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
import android.widget.TextView;

import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.helper.MappingHelper;
import com.rahmanarif.filmcatalog.model.Movie;

import java.util.ArrayList;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.CONTENT_URI;
import static com.rahmanarif.filmcatalog.ui.activity.DetailActivity.EXTRA_RESULT;
import static com.rahmanarif.filmcatalog.ui.activity.DetailActivity.RESULT_DELETE;

public class FavoriteFragment extends Fragment {
    private static final String STATE = "statete";
    private RecyclerView listFavoriteFilm;
    private ProgressBar progressBar;
    private TextView tvNoList;

    private MovieAdapter adapter;
    private ArrayList<Movie> movies = new ArrayList<>();

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        listFavoriteFilm = v.findViewById(R.id.listFavoritFilm);
        progressBar = v.findViewById(R.id.progressBar);
        tvNoList = v.findViewById(R.id.no_content_text);

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
        progressBar.setVisibility(View.GONE);
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            movies = MappingHelper.mapCursorToArrayList(cursor);
            tvNoList.setVisibility(View.GONE);

            adapter = new MovieAdapter(movies);
            listFavoriteFilm.setLayoutManager(new LinearLayoutManager(getContext()));
            listFavoriteFilm.setAdapter(adapter);
        }
    }
}