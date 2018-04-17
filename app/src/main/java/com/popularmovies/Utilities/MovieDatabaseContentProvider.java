package com.popularmovies.Utilities;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;

public class MovieDatabaseContentProvider extends ContentProvider {

    private MovieDatabaseHelper database;

    private static final int MOVIE = 10;
    private static final int MOVIE_ID = 20;
    private static final String BASE_PATH = "movie";

    private static final UriMatcher sURIMathcer = new UriMatcher(UriMatcher.NO_MATCH);


    @Override
    public boolean onCreate() {
        database = new MovieDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        queryBuilder.setTables(MovieTable.TABLE_MOVIE);
        int uriType = sURIMathcer.match(uri);
        switch (uriType){
            case MOVIE:
                break;
            case MOVIE_ID:
                queryBuilder.appendWhere(MovieTable.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
        }
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = database.getWritableDatabase();
        int match =  sURIMathcer.match(uri);
        long id = 0;
        switch (match){
            case MOVIE:
                id = db.insert(MovieTable.TABLE_MOVIE, null, contentValues);
                break;
        }
        //Toast.makeText(getContext(), id + "", Toast.LENGTH_SHORT).show();
        uri = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    private void checkColumns(String[] protection){
        String[] available = {MovieTable.COLUMN_TITLE,
        MovieTable.COLUMN_IMAGE,
        MovieTable.COLUMN_OVERVIEW,
        MovieTable.COLUMN_RATING,
        MovieTable.COLUMN_RELEASE,
        MovieTable.COLUMN_ID };

        if (protection != null){
            HashSet<String> reqeust = new HashSet<>(Arrays.asList(protection));
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(available));
            if (!availableColumns.containsAll(reqeust))
                throw new IllegalArgumentException("Unknown columns");
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
