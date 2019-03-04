package com.rahmanarif.filmcatalog;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rahmanarif.filmcatalog.ui.activity.SettingsActivity;
import com.rahmanarif.filmcatalog.ui.fragment.FavoriteFragment;
import com.rahmanarif.filmcatalog.ui.fragment.NowPlayingFragment;
import com.rahmanarif.filmcatalog.ui.fragment.PopulerFragment;
import com.rahmanarif.filmcatalog.ui.fragment.TvShowFragment;
import com.rahmanarif.filmcatalog.ui.fragment.UpComingFragment;

public class MainActivity extends AppCompatActivity {

    private static final String STATE = "state";
    private BottomNavigationView bottomNavigationView;

    private Fragment fragment = new PopulerFragment();
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.populer_movie_menu:
                        fragment = new PopulerFragment();
                        showFragment(fragment);
                        return true;
                    case R.id.now_playing_movie_menu:
                        fragment = new NowPlayingFragment();
                        showFragment(fragment);
                        return true;
                    case R.id.up_coming_movie_menu:
                        fragment = new UpComingFragment();
                        showFragment(fragment);
                        return true;
                    case R.id.tv_show_menu:
                        fragment = new TvShowFragment();
                        showFragment(fragment);
                        return true;
                    case R.id.favorite_menu:
                        fragment = new FavoriteFragment();
                        showFragment(fragment);
                        return true;
                    default:
                        return false;
                }
            }
        });

        if (savedInstanceState == null) {
            //fragment yang pertama kali muncul setelah pop-up
            showFragment(fragment);
        } else {
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, STATE);
            showFragment(fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_languange) {
            Intent mIntent = new Intent(this, SettingsActivity.class);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getSupportFragmentManager().putFragment(outState, STATE, fragment);
        super.onSaveInstanceState(outState);
    }
}
