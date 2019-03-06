package com.rahmanarif.favoritelist.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahmanarif.favoritelist.BuildConfig;
import com.rahmanarif.favoritelist.R;
import com.rahmanarif.favoritelist.adapter.GenreAdapter;
import com.rahmanarif.favoritelist.api.ApiClient;
import com.rahmanarif.favoritelist.api.ApiService;
import com.rahmanarif.favoritelist.model.Genre;
import com.rahmanarif.favoritelist.model.Movie;
import com.rahmanarif.favoritelist.model.TvShow;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rahmanarif.favoritelist.adapter.MovieAdapter.TYPE_MOVIE;
import static com.rahmanarif.favoritelist.adapter.TvShowAdapter.TYPE_TV;

public class DetailActivity extends AppCompatActivity {

    private TextView judulFilm, taglineFilm, ratingFilm, durationFilm, langFilm, releaseFilm, overviewFilm;
    private ImageView posterFilm;
    private RecyclerView listGenre;

    private GenreAdapter adapter;

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    public static final String EXTRA_TYPE = "extra_type";
    private String movieId, typeIntent;
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

        movieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);
        typeIntent = getIntent().getStringExtra(EXTRA_TYPE);

        if (typeIntent.equalsIgnoreCase(TYPE_MOVIE)) {
            loadDataMovie();
        } else if (typeIntent.equalsIgnoreCase(TYPE_TV)) {
            loadDataTvShow();
        }
    }

    private void loadDataMovie() {
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

    private void loadDataTvShow() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<TvShow> call = apiService.getDetailsTv(movieId, BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<TvShow>() {
            @Override
            public void onResponse(Call<TvShow> call, Response<TvShow> response) {
                try {
                    if (response.body() != null) {
                        judulFilm.setText(response.body().getName());
                        ratingFilm.setText(response.body().getVoteAverage().toString());
                        durationFilm.setText(response.body().getEpisodeRunTime().get(0).toString());
                        langFilm.setText(response.body().getOriginalLanguage());
                        releaseFilm.setText(dateFormatter(response.body().getFirstAirDate()));
                        overviewFilm.setText(response.body().getOverview());
                        Picasso.get().load(BuildConfig.BASE_IMAGE_URL_ORIGINAL + response.body().getPosterPath())
                                .into(posterFilm);

                        genres = response.body().getGenres();
                        adapter = new GenreAdapter(genres);
                        listGenre.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        listGenre.setAdapter(adapter);
                        if (getSupportActionBar() != null)
                            getSupportActionBar().setTitle(judulFilm.getText().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TvShow> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return false;
        }
    }

    private String dateFormatter(String tanggal) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = dt.parse(tanggal);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return format.format(date);
    }
}
