package com.example.mvp.movie_list

import com.example.mvp.model.Movie

class MovieListPresenter(private var movieListView: MovieListContract.View?) :
    MovieListContract.Presenter,
    MovieListContract.Model.OnFinishedListener {

    private val movieListModel = MovieListModel()

    override fun onDestroy() {
        movieListView = null
    }

    override fun getMoreData(pageNo: Int) {
        movieListView?.showProgress()
        movieListModel.getMovieList(this, pageNo)
    }

    override fun requestDataFromServer() {
        movieListView?.showProgress()
        movieListModel.getMovieList(this, 1)
    }

    override fun onFinished(movieArrayList: List<Movie>?) {
        movieListView?.let {
            it.setDataToRecyclerView(movieArrayList)
            it.hideProgress()
        }
    }

    override fun onFailure(t: Throwable?) {
        movieListView?.let {
            it.onResponseFailure(t)
            it.hideProgress()
        }
    }
}