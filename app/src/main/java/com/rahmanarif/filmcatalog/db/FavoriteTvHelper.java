package com.rahmanarif.filmcatalog.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.TvTable.TV_ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.TvTable.TABLE_TV_FAVORITE;

public class FavoriteTvHelper {
    private Context context;
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private static FavoriteTvHelper INSTANCE;

    public FavoriteTvHelper(Context context) {
        this.context = context;
    }

    public static FavoriteTvHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new FavoriteTvHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public FavoriteTvHelper open() throws SQLException {
        helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (database.isOpen()) database.close();
    }
    public Cursor queryByIdProvider(String id) {
        return database.query(TABLE_TV_FAVORITE, null
                , TV_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(TABLE_TV_FAVORITE
                , null
                , null
                , null
                , null
                , null
                , _ID + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(TABLE_TV_FAVORITE, null, values);
    }

    public int updateProvider(String id, ContentValues values) {
        return database.update(TABLE_TV_FAVORITE, values, TV_ID + " = ?", new String[]{id});
    }

    public int deleteProvider(String id) {
        return database.delete(TABLE_TV_FAVORITE, TV_ID + " = ?", new String[]{id});
    }

}
