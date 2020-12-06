package com.georgebardas.popularmovies.util

import com.georgebardas.popularmovies.model.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("/3/movie/now_playing")
    fun getRecentMovies(@Query("api_key") key: String): Call<GetMoviesResponse>

    @GET("/3/movie/popular")
    fun getPopularMovies(@Query("api_key") key: String): Call<GetMoviesResponse>

}