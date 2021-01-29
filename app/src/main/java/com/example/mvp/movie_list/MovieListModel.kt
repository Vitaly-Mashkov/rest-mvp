package com.example.mvp.movie_list

import com.example.mvp.model.MovieListResponse
import com.example.mvp.network.ApiClient
import com.example.mvp.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieListModel : MovieListContract.Model {

    override fun getMovieList(
        onFinishedListener: MovieListContract.Model.OnFinishedListener?,
        pageNo: Int
    ) {
        val apiService = ApiClient.buildService(ApiInterface::class.java)
        val call = apiService.getPopularMovies(ApiClient.API_KEY, pageNo)

        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse?>,
                response: Response<MovieListResponse>
            ) {
                val movies = response.body()?.results
                onFinishedListener?.onFinished(movies)
            }

            override fun onFailure(call: Call<MovieListResponse?>, t: Throwable) {
                onFinishedListener?.onFailure(t)
            }
        })
    }


}