package com.popularmovies.Utilities;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class MovieTable {

    public static final String TABLE_MOVIE = "movie";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_OVERVIEW= "overview";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_RELEASE = "release";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_MOVIE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_IMAGE + " text not null, "
            + COLUMN_OVERVIEW + " blob not null, "
            + COLUMN_RATING + " float not null, "
            + COLUMN_RELEASE + " text not null"
            + ");";

    public static void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(database);
    }
}
