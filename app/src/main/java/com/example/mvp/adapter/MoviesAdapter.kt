package com.example.mvp.adapter

import Movie
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.mvp.R
import com.example.mvp.movie_list.MovieListActivity
import com.example.mvp.network.ApiClient

class MoviesAdapter(private val movieListActivity: MovieListActivity, private val movieList: List<Movie?>) : RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = movieList[position]
        holder.tvMovieTitle.text = movie?.title
        holder.tvMovieRatings.text = movie?.vote_average.toString()
        holder.tvReleaseDate.text = movie?.release_date
        Glide.with(movieListActivity)
            .load(ApiClient.IMAGE_BASE_URL + movie?.poster_path)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable?>, isFirstResource: Boolean): Boolean {
                    holder.pbLoadImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: com.bumptech.glide.request.target.Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    holder.pbLoadImage.visibility = View.GONE
                    return false
                }
            })
            .apply(
                RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
            )
            .into(holder.ivMovieThumb)
    }

    override fun getItemCount() =  movieList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvMovieTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
        var tvMovieRatings: TextView = itemView.findViewById(R.id.tv_movie_ratings)
        var tvReleaseDate: TextView = itemView.findViewById(R.id.tv_release_date)
        var ivMovieThumb: ImageView = itemView.findViewById(R.id.iv_movie_thumb)
        var pbLoadImage: ProgressBar = itemView.findViewById(R.id.pb_load_image)

    }
}
