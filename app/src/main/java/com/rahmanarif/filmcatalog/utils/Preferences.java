package com.rahmanarif.filmcatalog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rahmanarif.filmcatalog.R;

public class Preferences {
    SharedPreferences preferences;
    Context context;

    public Preferences(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setDaily(Boolean input){
        SharedPreferences.Editor editor = preferences.edit();
        String key = context.getResources().getString(R.string.set_daily_on);
        editor.putBoolean(key, input);
        editor.commit();
    }
    public Boolean getDailyOn(){
        String key = context.getResources().getString(R.string.set_daily_on);
        return preferences.getBoolean(key, true);
    }

    public void setRelease(Boolean input){
        SharedPreferences.Editor editor = preferences.edit();
        String key = context.getResources().getString(R.string.set_release_on);
        editor.putBoolean(key, input);
        editor.commit();
    }
    public Boolean getReleaseOn(){
        String key = context.getResources().getString(R.string.set_release_on);
        return preferences.getBoolean(key, true);
    }

}
