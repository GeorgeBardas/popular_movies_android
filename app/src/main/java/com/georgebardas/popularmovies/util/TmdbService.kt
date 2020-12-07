package com.georgebardas.popularmovies.util

import com.georgebardas.popularmovies.model.GetMoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbService {

    @GET("/3/movie/now_playingg")
    fun getRecentMovies(@Query("api_key") key: String): Call<GetMoviesResponse>

    @GET("/3/movie/popularr")
    fun getPopularMovies(@Query("api_key") key: String): Call<GetMoviesResponse>

    @GET("/3/movie/top_ratedd")
    fun getTopRatedMovies(@Query("api_key") key: String): Call<GetMoviesResponse>

}