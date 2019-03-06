package com.rahmanarif.filmcatalog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.MOVIE_ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TABLE_MOVIE_FAVORITE;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable._ID;

public class FavoriteMovieHelper {
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private static FavoriteMovieHelper INSTANCE;

    public FavoriteMovieHelper(Context context) {
        this.context = context;
    }

    public static FavoriteMovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteMovieHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public FavoriteMovieHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (database.isOpen()) database.close();
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(TABLE_MOVIE_FAVORITE, null
                , MOVIE_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(TABLE_MOVIE_FAVORITE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(TABLE_MOVIE_FAVORITE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(TABLE_MOVIE_FAVORITE, values, MOVIE_ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(TABLE_MOVIE_FAVORITE, MOVIE_ID + " = ?", new String[]{id});
    }

}
