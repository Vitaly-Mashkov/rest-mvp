package com.example.mvp.network

import MovieListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String?, @Query("page") PageNo: Int): Call<MovieListResponse>

}