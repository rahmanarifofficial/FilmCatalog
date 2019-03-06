package com.rahmanarif.favoritelist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rahmanarif.favoritelist.adapter.MovieAdapter;
import com.rahmanarif.favoritelist.adapter.TabFavoritePagerAdapter;
import com.rahmanarif.favoritelist.helper.MappingHelper;
import com.rahmanarif.favoritelist.model.Movie;
import com.rahmanarif.favoritelist.ui.fragment.MovieFavoriteFragment;
import com.rahmanarif.favoritelist.ui.fragment.TvFavoriteFragment;

import java.util.ArrayList;

import static com.rahmanarif.favoritelist.db.DatabaseContract.FilmTable.CONTENT_URI;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager_favorite);
        tabLayout = findViewById(R.id.tab_favorite);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        TabFavoritePagerAdapter adapter = new TabFavoritePagerAdapter(getSupportFragmentManager());

        Fragment movie = new MovieFavoriteFragment();
        adapter.addFragment(movie, getString(R.string.favorite_movie));

        Fragment tv = new TvFavoriteFragment();
        adapter.addFragment(tv, getString(R.string.favorite_tv));

        viewPager.setAdapter(adapter);
    }

}
