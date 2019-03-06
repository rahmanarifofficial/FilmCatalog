package com.rahmanarif.filmcatalog.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.rahmanarif.filmcatalog.BuildConfig;
import com.rahmanarif.filmcatalog.R;
import com.rahmanarif.filmcatalog.helper.MappingHelper;
import com.rahmanarif.filmcatalog.model.Movie;

import java.util.ArrayList;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.CONTENT_URI;
import static com.rahmanarif.filmcatalog.widget.FavoriteWidget.EXTRA_ITEM;
import static com.rahmanarif.filmcatalog.widget.FavoriteWidget.EXTRA_MOVIE_ID;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private ArrayList<Movie> widgetItems = new ArrayList<>();
    private ArrayList<Uri> posterPath = new ArrayList<>();
    private Bitmap bmp = null;

    private final Context context;
    private String movieId;
    private final String STATE = "statete";

    public StackRemoteViewsFactory(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    public void onCreate() {
        getFavorite();
    }

    @Override
    public void onDataSetChanged() {
        final long identityToken = Binder.clearCallingIdentity();
        getFavorite();
        Binder.restoreCallingIdentity(identityToken);

        for (int i = 0; i < widgetItems.size(); i++) {
            posterPath.add(Uri.parse(BuildConfig.BASE_IMAGE_URL_w342 + widgetItems.get(i).getPosterPath()));
            Log.d(STATE, posterPath.get(i).toString());
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return posterPath.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        movieId = widgetItems.get(position).getId().toString();
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.item_widget);

        try {
            bmp = Glide.with(context).asBitmap().load(posterPath.get(position)).into(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL, com.bumptech.glide.request.target.Target.SIZE_ORIGINAL).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        rv.setImageViewBitmap(R.id.fav_image, bmp);

        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ITEM, position);
        extras.putString(EXTRA_MOVIE_ID, movieId);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.fav_image, fillIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private void getFavorite() {
        Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            widgetItems = MappingHelper.mapCursorToMovieArrayList(cursor);
        }
    }

}
