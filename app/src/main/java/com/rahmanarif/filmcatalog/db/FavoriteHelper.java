package com.rahmanarif.filmcatalog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.MOVIE_ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TABLE_FAVORITE;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable._ID;

public class FavoriteHelper {
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private static FavoriteHelper INSTANCE;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public static FavoriteHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public FavoriteHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (database.isOpen()) database.close();
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(TABLE_FAVORITE, null
                , MOVIE_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(TABLE_FAVORITE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(TABLE_FAVORITE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(TABLE_FAVORITE, values, MOVIE_ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(TABLE_FAVORITE, MOVIE_ID + " = ?", new String[]{id});
    }

}
