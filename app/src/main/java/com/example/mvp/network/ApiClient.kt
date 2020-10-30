package com.example.mvp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "2e901364c3d103dcb00ced520e9bca3c"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w200/"
    const val BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w780/"

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}
