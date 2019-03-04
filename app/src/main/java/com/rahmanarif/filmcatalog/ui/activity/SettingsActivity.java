package com.rahmanarif.filmcatalog.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.ResultMovie;
import com.rahmanarif.filmcatalog.notification.NotificationReceiver;
import com.rahmanarif.filmcatalog.utils.Preferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    //TODO: MASALAH NOTIFIKASI YANG MUNCUL SEBELUM WAKTUNYA
    private String TAG_TANGGAL = "TANGGAL_TAG";

    private String testTanggal = "2019-01-03";

    private NotificationReceiver notificationReceiver;

    private Switch switchDailyReminder, switchReleaseReminder;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Movie> releasemovie = new ArrayList<>();
    private Preferences prefs1, prefs2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setReleaseNotification();
        switchDailyReminder = findViewById(R.id.switchDailyReminder);
        switchReleaseReminder = findViewById(R.id.switchReleaseReminder);

        notificationReceiver = new NotificationReceiver();
        prefs1 = new Preferences(SettingsActivity.this);
        prefs2 = new Preferences(SettingsActivity.this);

        switchDailyReminder.setChecked(prefs1.getDailyOn());
        switchDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs1.setDaily(isChecked);
                if (isChecked) {
                    notificationReceiver.setDailyNotification(SettingsActivity.this, NotificationReceiver.TYPE_DAILY_REMINDER);
                } else {
                    notificationReceiver.cancelNotification(SettingsActivity.this, NotificationReceiver.TYPE_DAILY_REMINDER);
                }
            }
        });
        switchReleaseReminder.setChecked(prefs2.getReleaseOn());
        switchReleaseReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs2.setRelease(isChecked);
                if (isChecked) {
                    setReleaseNotification();
                } else {
                    notificationReceiver.cancelNotification(SettingsActivity.this, NotificationReceiver.TYPE_RELEASE_REMINDER);
                }
            }
        });
    }

    private void setReleaseNotification() {
        Log.d(TAG_TANGGAL, getNowDate());
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResultMovie> call = apiService.getMovieUpComing(BuildConfig.TSDB_API_KEY);

        call.enqueue(new Callback<ResultMovie>() {
            @Override
            public void onResponse(Call<ResultMovie> call, Response<ResultMovie> response) {
                if (response.body() != null) {
                    movies = response.body().getResults();
                    Log.d(TAG_TANGGAL, Integer.toString(movies.size()));
                    for (Movie movie : movies) {
                        Log.d(TAG_TANGGAL, movie.getReleaseDate());
                        if (getNowDate().equalsIgnoreCase(movie.getReleaseDate())) {
                            releasemovie.add(movie);
                        }
                    }
                    notificationReceiver.setReleaseNotification(SettingsActivity.this, NotificationReceiver.TYPE_RELEASE_REMINDER,
                            releasemovie);
                }
            }

            @Override
            public void onFailure(Call<ResultMovie> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private String getNowDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void changeLanguange(View views) {
        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
    }
}
