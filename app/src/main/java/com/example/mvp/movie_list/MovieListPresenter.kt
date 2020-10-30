package com.example.mvp.movie_list

import com.example.mvp.model.Movie

class MovieListPresenter(private var movieListView: MovieListContract.View?) : MovieListContract.Presenter,
    MovieListContract.Model.OnFinishedListener {

    private val movieListModel: MovieListContract.Model

    override fun onDestroy() {
        movieListView = null
    }

    override fun requestDataFromServer() {
        movieListView!!.showProgress()
        movieListModel.getMovieList(this, 1)
    }

    override fun onFinished(movieArrayList: List<Movie>?) {
        movieListView!!.setDataToRecyclerView(movieArrayList)
        movieListView!!.hideProgress()

    }

    override fun onFailure(t: Throwable?) {
        movieListView!!.onResponseFailure(t)
        movieListView!!.hideProgress()
    }

    init {
        movieListModel = MovieListModel()
    }
}