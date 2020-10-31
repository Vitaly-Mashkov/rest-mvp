package com.example.mvp.movie_details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.mvp.R
import com.example.mvp.adapter.CastAdapter
import com.example.mvp.model.Cast
import com.example.mvp.model.Movie
import com.example.mvp.network.ApiClient
import com.example.mvp.utils.Constants.KEY_MOVIE_ID
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.content_movie_details.*

class MovieDetailsActivity : AppCompatActivity(), MovieDetailsContract.View {
    private var castAdapter: CastAdapter? = null
    private val castList = mutableListOf<Cast>()
    private var movieName: String? = null
    private var movieDetailsPresenter: MovieDetailsPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
        initCollapsingToolbar()
        initUI()
        val mIntent = intent
        val movieId = mIntent.getIntExtra(KEY_MOVIE_ID, 0)
        movieDetailsPresenter = MovieDetailsPresenter(this)
        movieDetailsPresenter!!.requestMovieData(movieId)
    }

    private fun initUI() {
        castAdapter = CastAdapter(this, castList)
        rv_cast.adapter = castAdapter
    }


    private fun initCollapsingToolbar() {
        collapsing_toolbar.title = " "
        val appBarLayout = findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout.setExpanded(true)

        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsing_toolbar.title = movieName
                    isShow = true
                } else if (isShow) {
                    collapsing_toolbar.title = " "
                    isShow = false
                }
            }
        })
    }

    override fun showProgress() {
        pb_load_backdrop.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        pb_cast_loading.visibility = View.GONE
    }

    override fun setDataToViews(movie: Movie) {
        movieName = movie.title
        tv_movie_title.text = movie.title
        tv_release_date.text = movie.release_date
        tv_movie_ratings.text = movie.vote_average.toString()
        tv_movie_overview.text = movie.overview

        Glide.with(this)
                .load(ApiClient.BACKDROP_BASE_URL + movie.backdrop_path)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                        pb_load_backdrop.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        pb_load_backdrop.visibility = View.GONE
                        return false
                    }
                })
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_place_holder))
                .into(iv_backdrop)
        castList.clear()
        castList.addAll(movie.credits.cast)
        castAdapter?.notifyDataSetChanged()
        tv_tagline_value.text = movie.tagline
        tv_homepage_value.text = movie.homepage
        tv_runtime_value.text = movie.runtime
    }

    override fun onResponseFailure(throwable: Throwable) {
        Snackbar.make(findViewById(R.id.main_content),
                getString(R.string.error_data), Snackbar.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        movieDetailsPresenter!!.onDestroy()
    }
}