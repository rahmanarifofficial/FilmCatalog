package com.rahmanarif.favoritelist.ui.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rahmanarif.favoritelist.R;
import com.rahmanarif.favoritelist.adapter.TvShowAdapter;
import com.rahmanarif.favoritelist.helper.MappingHelper;
import com.rahmanarif.favoritelist.model.TvShow;

import java.util.ArrayList;

import static com.rahmanarif.favoritelist.db.DatabaseContract.TvTable.CONTENT_URI2;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFavoriteFragment extends Fragment {


    private RecyclerView listFavoriteTvShows;
    private ProgressBar progressBar;
    private TextView tvNoList;

    private TvShowAdapter adapterTv;
    private ArrayList<TvShow> tvShows = new ArrayList<>();

    public TvFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tv_favorite, container, false);
        listFavoriteTvShows = v.findViewById(R.id.listFavoritTV);
        progressBar = v.findViewById(R.id.progressBar);
        tvNoList = v.findViewById(R.id.no_content_tv_text);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d("statusstate", "created");
        super.onActivityCreated(savedInstanceState);
        loadMovie();
    }

    @Override
    public void onResume() {
        Log.d("statusstate", "OK");
        super.onResume();
        loadMovie();
    }

    private void loadMovie() {
        progressBar.setVisibility(View.GONE);
        Cursor cursor = getContext().getContentResolver().query(CONTENT_URI2, null, null, null, null);
        if (cursor.getCount() > 0) {
            tvShows = MappingHelper.mapCursorToTvArrayList(cursor);
            tvNoList.setVisibility(View.GONE);

            adapterTv = new TvShowAdapter(tvShows);
            listFavoriteTvShows.setLayoutManager(new LinearLayoutManager(getContext()));
            listFavoriteTvShows.setAdapter(adapterTv);
        }
    }

}
