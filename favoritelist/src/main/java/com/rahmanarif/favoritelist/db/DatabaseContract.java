package com.rahmanarif.favoritelist.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.rahmanarif.filmcatalog";
    private static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static final class FilmTable implements BaseColumns {
        public static final String TABLE_FAVORITE = "table_favorite";
        public static final String MOVIE_ID = "movie_id";
        public static final String TITLE = "tilte";
        public static final String OVERVIEW = "overview";
        public static final String POSTERURI = "posteruri";
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

}
