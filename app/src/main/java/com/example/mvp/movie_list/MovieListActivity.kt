package com.example.mvp.movie_list

import Movie
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvp.R
import com.example.mvp.adapter.MoviesAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MovieListActivity : AppCompatActivity(), MovieListContract.View {

    private var movieListPresenter: MovieListPresenter? = null

    private val moviesList = mutableListOf<Movie>()
    private var moviesAdapter: MoviesAdapter? = null
//    private var pageNo = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = getString(R.string.most_popular_movies)
        initUI()
        movieListPresenter = MovieListPresenter(this)
        movieListPresenter?.requestDataFromServer()
    }

    private fun initUI() {
        moviesAdapter = MoviesAdapter(this, moviesList)
        val mLayoutManager = GridLayoutManager(this, 2)
        rv_movie_list.apply {
            layoutManager = mLayoutManager
            rv_movie_list.adapter = moviesAdapter
        }

    }


    override fun showProgress() {
        pb_loading.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pb_loading.visibility = View.GONE
    }

    override fun setDataToRecyclerView(movieArrayList: List<Movie>?) {
        moviesList.addAll(movieArrayList!!)
        moviesAdapter!!.notifyDataSetChanged()
//        pageNo++
    }

    override fun onResponseFailure(throwable: Throwable?) {
        Log.e(TAG, throwable?.message!!)
        Toast.makeText(this, getString(R.string.communication_error), Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListPresenter?.onDestroy()
    }

    companion object {
        private const val TAG = "MovieListActivity"
    }
}
