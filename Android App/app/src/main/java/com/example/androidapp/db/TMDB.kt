package com.example.androidapp.db


import retrofit2.http.GET
import retrofit2.http.Query

interface TMDB {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = ApiKey.API_KEY
    ): FilmResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = ApiKey.API_KEY
    ): FilmResponse
}
