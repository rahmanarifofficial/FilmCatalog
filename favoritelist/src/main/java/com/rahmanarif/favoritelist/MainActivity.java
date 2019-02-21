package com.rahmanarif.favoritelist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rahmanarif.favoritelist.adapter.MovieAdapter;
import com.rahmanarif.favoritelist.helper.MappingHelper;
import com.rahmanarif.favoritelist.model.Movie;

import java.util.ArrayList;

import static com.rahmanarif.favoritelist.db.DatabaseContract.FilmTable.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listFavoriteFilm;
    private ProgressBar progressBar;
    private TextView tvNoContent;

    private MovieAdapter adapter;
    private ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listFavoriteFilm = findViewById(R.id.listFavoritFilm);
        progressBar = findViewById(R.id.progressBar);
        tvNoContent = findViewById(R.id.no_content_text);

        loadMovie();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovie();
    }

    private void loadMovie() {
        progressBar.setVisibility(View.GONE);
        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            movies = MappingHelper.mapCursorToArrayList(cursor);
            tvNoContent.setVisibility(View.GONE);
            adapter = new MovieAdapter(movies);
            for (Movie x : movies) {
                Log.d("listmovie", x.getTitle());
                Log.d("listmovie", x.getId().toString());
                Log.d("listmovie", x.getOverview());
                Log.d("listmovie", x.getPosterPath());
            }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            listFavoriteFilm.setLayoutManager(layoutManager);
            listFavoriteFilm.setAdapter(adapter);
        }
    }
}
