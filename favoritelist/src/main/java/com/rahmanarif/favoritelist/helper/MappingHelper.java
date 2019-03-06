package com.rahmanarif.favoritelist.helper;

import android.database.Cursor;

import com.rahmanarif.favoritelist.model.Movie;
import com.rahmanarif.favoritelist.model.TvShow;

import java.util.ArrayList;

import static com.rahmanarif.favoritelist.db.DatabaseContract.FilmTable.MOVIE_ID;
import static com.rahmanarif.favoritelist.db.DatabaseContract.FilmTable.OVERVIEW;
import static com.rahmanarif.favoritelist.db.DatabaseContract.FilmTable.POSTERURI;
import static com.rahmanarif.favoritelist.db.DatabaseContract.FilmTable.TITLE;
import static com.rahmanarif.favoritelist.db.DatabaseContract.TvTable.TV_ID;
import static com.rahmanarif.favoritelist.db.DatabaseContract.TvTable.TV_NAME;
import static com.rahmanarif.favoritelist.db.DatabaseContract.TvTable.TV_OVERVIEW;
import static com.rahmanarif.favoritelist.db.DatabaseContract.TvTable.TV_POSTERURI;

public class MappingHelper {
    public static ArrayList<Movie> mapCursorToArrayList(Cursor cursor) {
        ArrayList<Movie> movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            int movieid = cursor.getInt(cursor.getColumnIndexOrThrow(MOVIE_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW));
            String poster_uri = cursor.getString(cursor.getColumnIndexOrThrow(POSTERURI));
            movies.add(new Movie(movieid, title, poster_uri, overview));
        }
        return movies;
    }

    public static ArrayList<TvShow> mapCursorToTvArrayList(Cursor cursor) {
        ArrayList<TvShow> tvShows = new ArrayList<>();
        while (cursor.moveToNext()) {
            int tvid = cursor.getInt(cursor.getColumnIndexOrThrow(TV_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(TV_NAME));
            String overview = cursor.getString(cursor.getColumnIndexOrThrow(TV_OVERVIEW));
            String poster_uri = cursor.getString(cursor.getColumnIndexOrThrow(TV_POSTERURI));
            tvShows.add(new TvShow(tvid, name, poster_uri, overview));
        }
        return tvShows;
    }

}
