package com.rahmanarif.filmcatalog.ui.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.adapter.MovieAdapter;
import com.rahmanarif.filmcatalog.adapter.TabFavoritePagerAdapter;
import com.rahmanarif.filmcatalog.adapter.TvShowAdapter;
import com.rahmanarif.filmcatalog.helper.MappingHelper;
import com.rahmanarif.filmcatalog.model.Movie;
import com.rahmanarif.filmcatalog.model.TvShow;

import java.util.ArrayList;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.CONTENT_URI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.TvTable.CONTENT_URI2;

public class FavoriteFragment extends Fragment {
    private static final String STATE = "statete";
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public FavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        viewPager = v.findViewById(R.id.view_pager_favorite);
        tabLayout = v.findViewById(R.id.tab_favorite);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        TabFavoritePagerAdapter adapter = new TabFavoritePagerAdapter(getFragmentManager());

        Fragment movie = new FavoriteMovieFragment();
        adapter.addFragment(movie, getString(R.string.favorite_movie));

        Fragment tv = new FavoriteTvFragment();
        adapter.addFragment(tv, getString(R.string.favorite_tv));

        viewPager.setAdapter(adapter);
    }

}
