package com.rahmanarif.filmcatalog.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.api.ApiClient;
import com.rahmanarif.filmcatalog.api.ApiService;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.Result;
import com.rahmanarif.filmcatalog.notification.NotificationReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements Callback<Result> {

    private String TAG_TANGGAL = "TANGGAL_TAG";

    private String timeDailyReminder = "07:00";
    private String messageDailyReminder = "Silakan cek kembali Katalog Film, barangkali ada film baru";
    private String timeReleaseReminder = "08:00";
    private String messageReleaseReminder = "Silakn cek kembali Katalog Film, Film baru telah release";

    private boolean same = false;

    private int countSame = 0;
    private String dateRelease = "s";
    private String testTanggal = "2019-01-03";

    private NotificationReceiver notificationReceiver;

    private Switch switchDailyReminder, switchReleaseReminder;
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<String> releaseDate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchDailyReminder = findViewById(R.id.switchDailyReminder);
        switchReleaseReminder = findViewById(R.id.switchReleaseReminder);

//        if (isDateSame()) {
//            dateRelease = getNowDate();
//        }

        isDateSame();
        Log.d(TAG_TANGGAL, getDateRelease());

        notificationReceiver = new NotificationReceiver();

        switchDailyReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationReceiver.setDailyNotification(SettingsActivity.this, NotificationReceiver.TYPE_DAILY_REMINDER,
                            timeDailyReminder, messageDailyReminder);
                } else {
                    notificationReceiver.cancelNotification(SettingsActivity.this, NotificationReceiver.TYPE_DAILY_REMINDER);
                }
            }
        });
        switchReleaseReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notificationReceiver.setReleaseNotification(SettingsActivity.this, NotificationReceiver.TYPE_RELEASE_REMINDER,
                            dateRelease, timeReleaseReminder, messageReleaseReminder);
                } else {
                    notificationReceiver.cancelNotification(SettingsActivity.this, NotificationReceiver.TYPE_RELEASE_REMINDER);
                }
            }
        });
    }

    private void isDateSame() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Result> call = apiService.getMovieUpComing(BuildConfig.TSDB_API_KEY);

        call.enqueue(this);
    }

    public String getDateRelease() {
        return dateRelease;
    }

    public void setDateRelease(String dateRelease) {
        this.dateRelease = dateRelease;
    }

    private String getNowDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void changeLanguange(View views) {
        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
    }

    @Override
    public void onResponse(Call<Result> call, Response<Result> response) {
        if (response.body() != null) {
            movies = response.body().getResults();
            Log.d(TAG_TANGGAL, Integer.toString(movies.size()));
            for (int i = 0; i < movies.size(); i++) {
                releaseDate.add(movies.get(i).getReleaseDate());
                if (testTanggal.equalsIgnoreCase(releaseDate.get(i))) {
                    countSame++;
                }
                Log.d(TAG_TANGGAL, releaseDate.get(i));
                if (countSame > 0) {
                    setDateRelease(testTanggal);
                }
            }
            Log.d(TAG_TANGGAL, Integer.toString(countSame));
        }
    }

    @Override
    public void onFailure(Call<Result> call, Throwable t) {
        t.printStackTrace();
    }
}
