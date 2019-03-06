package com.rahmanarif.filmcatalog.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rahmanarif.filmcatalog.db.FavoriteMovieHelper;
import com.rahmanarif.filmcatalog.db.FavoriteTvHelper;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.AUTHORITY;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.CONTENT_URI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TABLE_MOVIE_FAVORITE;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.TvTable.CONTENT_URI2;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.TvTable.TABLE_TV_FAVORITE;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE_FAV = 1;
    private static final int MOVIE_FAV_ID = 2;
    private static final int TV_FAV = 3;
    private static final int TV_FAV_ID = 4;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private FavoriteMovieHelper movieHelper;
    private FavoriteTvHelper tvHelper;
    private static final String TAG = "STATETETE";


    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE_FAVORITE, MOVIE_FAV);
        sUriMatcher.addURI(AUTHORITY, TABLE_MOVIE_FAVORITE + "/#", MOVIE_FAV_ID);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV_FAVORITE, TV_FAV);
        sUriMatcher.addURI(AUTHORITY, TABLE_TV_FAVORITE + "/#", TV_FAV_ID);
    }

    @Override
    public boolean onCreate() {
        movieHelper = FavoriteMovieHelper.getInstance(getContext());
        tvHelper = FavoriteTvHelper.getInstance(getContext());
        movieHelper.open();
        tvHelper.open();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_FAV:
                cursor = movieHelper.queryProvider();
                break;
            case MOVIE_FAV_ID:
                cursor = movieHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            case TV_FAV:
                cursor = tvHelper.queryProvider();
                break;
            case TV_FAV_ID:
                cursor = tvHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri _uri = null;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_FAV:
                long _ID1 = movieHelper.insertProvider(values);
                if (_ID1 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI, _ID1);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
                break;
            case TV_FAV:
                Log.d(TAG, "masuk " + TV_FAV);
                long _ID2 = tvHelper.insertProvider(values);
                Log.d(TAG, "NILAI _ID2" + _ID2);
                if (_ID2 > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI2, _ID2);
                    getContext().getContentResolver().notifyChange(_uri, null);
                }
            default:
                break;
        }
        return _uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_FAV_ID:
                deleted = movieHelper.deleteProvider(uri.getLastPathSegment());
                if (deleted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case TV_FAV_ID:
                deleted = tvHelper.deleteProvider(uri.getLastPathSegment());
                if (deleted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                deleted = 0;
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_FAV_ID:
                updated = movieHelper.updateProvider(uri.getLastPathSegment(), values);
                if (updated > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case TV_FAV_ID:
                updated = tvHelper.updateProvider(uri.getLastPathSegment(), values);
                if (updated > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                updated = 0;
                break;
        }
        return updated;
    }
}
