package com.rahmanarif.filmcatalog.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Genre;
import com.rahmanarif.filmcatalog.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView judulFilm, taglineFilm, ratingFilm, durationFilm, langFilm, releaseFilm, overviewFilm, genreFilm;
    private ImageView posterFilm;
    private List<Genre> genres;

    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        judulFilm = findViewById(R.id.detailJudulFilm);
        taglineFilm = findViewById(R.id.detailTaglineFilm);
        ratingFilm = findViewById(R.id.detailRatingFilm);
        durationFilm = findViewById(R.id.detailDurationFilm);
        langFilm = findViewById(R.id.detailLanguangeFilm);
        releaseFilm = findViewById(R.id.detailReleaseFilm);
        overviewFilm = findViewById(R.id.detailOverviewFilm);
        posterFilm = findViewById(R.id.detailPosterFilm);
        genreFilm = findViewById(R.id.detailGenreFilm);

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
                    for (Genre data : genres){
                        genreFilm.setText(data.getName());
                    }

                    getSupportActionBar().setTitle(judulFilm.getText().toString());
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

    private String dateFormatter(String tanggal) throws ParseException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dt.parse(tanggal);
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy");
        return format.format(date);
    }
}
