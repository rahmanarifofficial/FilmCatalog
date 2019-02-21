package com.rahmanarif.filmcatalog.ui.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.rahmanarif.filmcatalog.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void changeLanguange(View views){
        startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
    }
}
