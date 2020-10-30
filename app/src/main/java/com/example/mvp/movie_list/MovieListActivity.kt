package com.example.mvp.movie_list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mvp.R
import com.example.mvp.adapter.MoviesAdapter
import com.example.mvp.model.Movie
import com.example.mvp.movie_details.MovieDetailsActivity
import com.example.mvp.movie_filter.MovieFilterActivity
import com.example.mvp.utils.Constants.ACTION_MOVIE_FILTER
import com.example.mvp.utils.Constants.KEY_MOVIE_ID
import com.example.mvp.utils.Constants.KEY_RELEASE_FROM
import com.example.mvp.utils.Constants.KEY_RELEASE_TO
import kotlinx.android.synthetic.main.activity_main.*

class MovieListActivity : AppCompatActivity(), MovieListContract.View, ShowEmptyView, MovieItemClickListener {

    private var movieListPresenter: MovieListPresenter? = null

    private val moviesList = mutableListOf<Movie>()
    private var moviesAdapter: MoviesAdapter? = null
//    private var pageNo = 1

    private var fromReleaseFilter = ""
    private var toReleaseFilter = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.title = getString(R.string.most_popular_movies)
        initUI()
        setListeners()
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

    private fun setListeners(){
        fab_filter.setOnClickListener {
            val intent = Intent(this, MovieFilterActivity::class.java)
            intent.putExtra(KEY_RELEASE_FROM, fromReleaseFilter)
            intent.putExtra(KEY_RELEASE_TO, toReleaseFilter)
            startActivityForResult(intent, ACTION_MOVIE_FILTER)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MOVIE_FILTER) {
            if (resultCode == RESULT_OK) {
                fromReleaseFilter = data?.getStringExtra(KEY_RELEASE_FROM)!!
                toReleaseFilter = data.getStringExtra(KEY_RELEASE_TO)!!
                moviesAdapter?.setFilterParameter(fromReleaseFilter, toReleaseFilter)
                moviesAdapter?.filter?.filter("")
            }
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

    override fun showEmptyView() {
        rv_movie_list.visibility = View.GONE
        tv_empty_view.visibility = View.VISIBLE
    }

    override fun hideEmptyView() {
        rv_movie_list.visibility = View.VISIBLE
        tv_empty_view.visibility = View.GONE
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra(KEY_MOVIE_ID, moviesList[position].id)
        startActivity(intent)
    }
}
