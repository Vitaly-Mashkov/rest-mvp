package com.example.mvp.movie_details

import android.util.Log
import com.example.mvp.model.Movie
import com.example.mvp.network.ApiClient
import com.example.mvp.network.ApiClient.API_KEY
import com.example.mvp.network.ApiInterface
import com.example.mvp.utils.Constants.CREDITS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailsModel : MovieDetailsContract.Model {
    private val TAG = "MovieDetailsModel"
    override fun getMovieDetails(onFinishedListener: MovieDetailsContract.Model.OnFinishedListener?, movieId: Int) {
        val apiService: ApiInterface = ApiClient.buildService(ApiInterface::class.java)
        val call: Call<Movie> = apiService.getMovieDetails(movieId, API_KEY, CREDITS)
        call.enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie?>?, response: Response<Movie?>) {
                val movie = response.body()
                Log.d(TAG, "Movie data received: $movie")
                onFinishedListener?.onFinished(movie!!)
            }

            override fun onFailure(call: Call<Movie?>?, t: Throwable) {
                Log.e(TAG, t.toString())
                onFinishedListener!!.onFailure(t)
            }
        })
    }
}