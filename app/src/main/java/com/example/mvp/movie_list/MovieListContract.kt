package com.example.mvp.movie_list

import com.example.mvp.model.Movie

interface MovieListContract {
    interface Model {
        interface OnFinishedListener {
            fun onFinished(movieArrayList: List<Movie>?)
            fun onFailure(t: Throwable?)
        }

        fun getMovieList(onFinishedListener: OnFinishedListener?, pageNo: Int)
    }

    interface View {
        fun showProgress()
        fun hideProgress()
        fun setDataToRecyclerView(movieArrayList: List<Movie>?)
        fun onResponseFailure(throwable: Throwable?)
    }

    interface Presenter {
        fun onDestroy()
        fun getMoreData(pageNo : Int)
        fun requestDataFromServer()
    }
}