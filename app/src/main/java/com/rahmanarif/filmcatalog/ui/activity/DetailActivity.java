package com.rahmanarif.filmcatalog.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.ArrayList;
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
    private String movieId, title, tagline, rating, duration, lang, release, overview, posterUri, posterpath;
    private List<Genre> genres;
    private boolean isFavorite = false;
    private Menu menuItem;

    private static final String STATE_JUDUL = "judul";
    private static final String STATE_TAGLINE = "tagline";
    private static final String STATE_RATING = "rating";
    private static final String STATE_DURASI = "durasi";
    private static final String STATE_LANG = "lang";
    private static final String STATE_RELEASE = "rilis";
    private static final String STATE_OVERVIEW = "overview";
    private static final String STATE_POSTER = "poster";
    private static final String STATE_GENRE = "genre";

    public static String EXTRA_RESULT = "test";
    public static final int RESULT_DELETE = 101;

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

        movieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);

        loadData();

        if (savedInstanceState != null) {
            Picasso.get().load(savedInstanceState.getString(STATE_POSTER)).into(posterFilm);
            judulFilm.setText(savedInstanceState.getString(STATE_JUDUL));
            taglineFilm.setText(savedInstanceState.getString(STATE_TAGLINE));
            ratingFilm.setText(savedInstanceState.getString(STATE_RATING));
            durationFilm.setText(savedInstanceState.getString(STATE_DURASI));
            langFilm.setText(savedInstanceState.getString(STATE_LANG));
            releaseFilm.setText(savedInstanceState.getString(STATE_RELEASE));
            overviewFilm.setText(savedInstanceState.getString(STATE_OVERVIEW));
        }
    }

    private void loadData() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Movie> call = apiService.getDetailsMovie(movieId, BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                try {
                    if (response.body() != null) {
                        posterpath = response.body().getPosterPath();
                        judulFilm.setText(response.body().getTitle());
                        taglineFilm.setText(response.body().getTagline());
                        ratingFilm.setText(response.body().getVoteAverage().toString());
                        durationFilm.setText(response.body().getRuntime().toString());
                        langFilm.setText(response.body().getOriginalLanguage());
                        releaseFilm.setText(dateFormatter(response.body().getReleaseDate()));
                        overviewFilm.setText(response.body().getOverview());
                        posterUri = BuildConfig.BASE_IMAGE_URL_ORIGINAL + posterpath;
                        Picasso.get().load(posterUri).into(posterFilm);

                        title = judulFilm.getText().toString().trim();
                        tagline = taglineFilm.getText().toString().trim();
                        rating = ratingFilm.getText().toString().trim();
                        duration = durationFilm.getText().toString().trim();
                        lang = langFilm.getText().toString().trim();
                        release = releaseFilm.getText().toString();
                        overview = overviewFilm.getText().toString().trim();


                        genres = response.body().getGenres();
                        adapter = new GenreAdapter(genres);
                        listGenre.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        listGenre.setAdapter(adapter);
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(title);
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
            case android.R.id.home:
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_POSTER, posterUri);
        outState.putString(STATE_JUDUL, title);
        outState.putString(STATE_DURASI, duration);
        outState.putString(STATE_LANG, lang);
        outState.putString(STATE_OVERVIEW, overview);
        outState.putString(STATE_RATING, rating);
        outState.putString(STATE_RELEASE, release);
        outState.putString(STATE_TAGLINE, tagline);
        super.onSaveInstanceState(outState);
    }

    private void setFavorite() {
        if (isFavorite) {
            menuItem.getItem(0).setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            menuItem.getItem(0).setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void removeFromFavorite() {
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
