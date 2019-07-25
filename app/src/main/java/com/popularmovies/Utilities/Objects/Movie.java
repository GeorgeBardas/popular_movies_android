package com.popularmovies.Utilities.Objects;

import android.content.ContentValues;
import android.database.Cursor;

import com.popularmovies.Utilities.DB.MovieTable;

public class Movie {

    int idDB;
    int id;
    String title;
    String image_link;
    String overview;
    String rating;
    String releaseDate;
    String genre;
    int runtime;

    public Movie() {
        super();
    }

    public Movie(String title) {
        this.title = title;
    }

    public Movie(int id, String title, String image_link, String overview, String rating, String releaseDate, String genre, int runtime) {
        this.id = id;
        this.title = title;
        this.image_link = image_link;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.genre = genre;
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();
        values.put("title", getTitle());
        values.put("image", getImage_link());
        values.put("overview", getOverview());
        values.put("rating", getRating());
        values.put("release", getReleaseDate());
        return values;
    }

    public Movie getMovieFromCursor(Cursor cursor){
        Movie movie = new Movie();
        movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_TITLE)));
        movie.setRating(cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_RATING)));
        movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_RELEASE)));
        movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_OVERVIEW)));
        movie.setImage_link(cursor.getString(cursor.getColumnIndex(MovieTable.COLUMN_IMAGE)));
        movie.setGenre(movie.title);
        return movie;
    }
}
