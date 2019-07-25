package com.popularmovies.Utilities.DB;

import com.popularmovies.Utilities.Objects.Movie;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private static List<Movie> movies = new ArrayList<>();

    private static Data instance = new Data();
    private Data(){}
    public static Data getInstance(){
        return instance;
    }

    public static void setMoviesList(List<Movie> movieList){
        movies.clear();
        movies.addAll(movieList);
    }

    public List<Movie> getMoviesList(){
        return movies;
    }
}
