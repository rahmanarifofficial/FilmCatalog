package com.rahmanarif.filmcatalog.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.MOVIE_ID;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.OVERVIEW;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.POSTERURI;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TABLE_FAVORITE;
import static com.rahmanarif.filmcatalog.db.DatabaseContract.FilmTable.TITLE;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "favorite.db";
    public static final int DATABASE_VERSION = 1;
    public static String CREATE_TABLE = "CREATE TABLE " + TABLE_FAVORITE + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MOVIE_ID + " TEXT NOT NULL, " +
            TITLE + " TEXT NOT NULL, " +
            OVERVIEW + " TEXT NOT NULL, " +
            POSTERURI + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }
}
