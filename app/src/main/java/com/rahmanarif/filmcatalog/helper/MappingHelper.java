package com.rahmanarif.filmcatalog.helper;

import android.database.Cursor;

import com.rahmanarif.filmcatalog.model.Movie;

import java.util.ArrayList;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.MOVIE_ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.OVERVIEW;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.POSTERURI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TITLE;

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
}
