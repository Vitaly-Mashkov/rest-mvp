package com.example.mvp.movie_details

import com.example.mvp.model.Movie

class MovieDetailsPresenter(private var movieDetailView: MovieDetailsContract.View?) : MovieDetailsContract.Presenter, MovieDetailsContract.Model.OnFinishedListener {
    private val movieDetailsModel: MovieDetailsContract.Model
    override fun onDestroy() {
        movieDetailView = null
    }

    override fun requestMovieData(movieId: Int) {
        if (movieDetailView != null) {
            movieDetailView!!.showProgress()
        }
        movieDetailsModel.getMovieDetails(this, movieId)
    }

    override fun onFinished(movie: Movie) {
        if (movieDetailView != null) {
            movieDetailView!!.hideProgress()
        }
        movieDetailView!!.setDataToViews(movie)
    }

    override fun onFailure(t: Throwable) {
        if (movieDetailView != null) {
            movieDetailView!!.hideProgress()
        }
        movieDetailView!!.onResponseFailure(t)
    }

    init {
        movieDetailsModel = MovieDetailsModel()
    }
}
