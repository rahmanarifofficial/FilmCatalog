package com.rahmanarif.filmcatalog.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.GenreAdapter;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Genre;
import com.rahmanarif.filmcatalog.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.CONTENT_URI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.MOVIE_ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.OVERVIEW;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.POSTERURI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TITLE;

public class DetailActivity extends AppCompatActivity {

    private TextView judulFilm, taglineFilm, ratingFilm, durationFilm, langFilm, releaseFilm, overviewFilm;
    private ImageView posterFilm;
    private RecyclerView listGenre;

    private GenreAdapter adapter;

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private String movieId, title, overview, posterpath;
    private List<Genre> genres;
    private boolean isFavorite = false;
    private Menu menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        judulFilm = findViewById(R.id.detailJudulFilm);
        taglineFilm = findViewById(R.id.detailTaglineFilm);
        ratingFilm = findViewById(R.id.detailRatingFilm);
        durationFilm = findViewById(R.id.detailDurationFilm);
        langFilm = findViewById(R.id.detailLanguangeFilm);
        releaseFilm = findViewById(R.id.detailReleaseFilm);
        overviewFilm = findViewById(R.id.detailOverviewFilm);
        posterFilm = findViewById(R.id.detailPosterFilm);
        listGenre = findViewById(R.id.detailListGenreFilm);

        loadData();
    }

    private void loadData() {
        movieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Movie> call = apiService.getDetailsMovie(movieId, BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                try {
                    if (response.body() != null) {
                        judulFilm.setText(response.body().getTitle());
                        taglineFilm.setText(response.body().getTagline());
                        ratingFilm.setText(response.body().getVoteAverage().toString());
                        durationFilm.setText(response.body().getRuntime().toString());
                        langFilm.setText(response.body().getOriginalLanguage());
                        releaseFilm.setText(dateFormatter(response.body().getReleaseDate()));
                        overviewFilm.setText(response.body().getOverview());
                        Picasso.get().load("https://image.tmdb.org/t/p/original" + response.body().getPosterPath())
                                .into(posterFilm);
                        title = judulFilm.getText().toString().trim();
                        overview = overviewFilm.getText().toString().trim();
                        posterpath = response.body().getPosterPath();
                        genres = response.body().getGenres();
                        adapter = new GenreAdapter(genres);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        listGenre.setLayoutManager(layoutManager);
                        listGenre.setAdapter(adapter);
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(judulFilm.getText().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        menuItem = menu;
        favoriteState(movieId);
        setFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                finish();
                return true;
            case R.id.add_to_favorite:
                if (isFavorite) {
                    removeFromFavorite();
                } else {
                    addToFavorite();
                }
                isFavorite = !isFavorite;
                setFavorite();
                return true;
            default:
                return false;
        }
    }

    private void setFavorite() {
        if (isFavorite) {
            menuItem.getItem(0).setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            menuItem.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void removeFromFavorite() {
        String selection = MOVIE_ID + " = ?";
        String[] arguments = {movieId};
        int uri = getContentResolver().delete(CONTENT_URI.buildUpon().appendPath(movieId).build(), null, null);
        Toast.makeText(this, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show();
        Log.d("cursor4", String.valueOf(uri));
    }

    private void addToFavorite() {
        ContentValues values = new ContentValues();
        values.put(MOVIE_ID, movieId);
        values.put(TITLE, title);
        values.put(OVERVIEW, overview);
        values.put(POSTERURI, posterpath);
        Toast.makeText(this, "Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show();
        Uri uri = getContentResolver().insert(CONTENT_URI, values);
        Log.d("cursor1", uri.toString());
    }

    private void favoriteState(String id) {
        String[] projection = {MOVIE_ID};
        String selection = MOVIE_ID + " = ?";
        String[] arguments = {movieId};
        Uri uri = CONTENT_URI.buildUpon().appendPath(id).build();
        Cursor cursor = getContentResolver().query(uri, projection, selection, arguments, null);
        if (cursor.getCount() > 0) {
            isFavorite = true;
        }
    }

    private String dateFormatter(String tanggal) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = dt.parse(tanggal);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return format.format(date);
    }
}
