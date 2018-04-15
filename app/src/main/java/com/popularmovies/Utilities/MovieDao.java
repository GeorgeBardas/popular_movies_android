package com.popularmovies.Utilities;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

        @Query("SELECT * FROM movie")
        List<Movie> getAllSavedMovies();

        @Query("SELECT * FROM movie WHERE title LIKE :title LIMIT 1")
        Movie getMovie(String title);

        @Insert
        void saveMovie(Movie movie);

        @Query("DELETE FROM movie WHERE title LIKE :title")
        void deleteMovie(String title);

        @Query("DELETE FROM movie")
        void emptyDatabase();
}
