package com.example.mvp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.mvp.R
import com.example.mvp.model.Movie
import com.example.mvp.movie_list.MovieListActivity
import com.example.mvp.network.ApiClient
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("UNCHECKED_CAST")
class MoviesAdapter(private val movieListActivity: MovieListActivity,
                    private var movieList: List<Movie?>) :
        RecyclerView.Adapter<MoviesAdapter.MyViewHolder>(), Filterable {

    private var fromDate = ""
    private var toDate = ""


    fun setFilterParameter(from: String, to: String){
        this.fromDate = from
        this.toDate = to
    }

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
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                    holder.pbLoadImage.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
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

        holder.itemView.setOnClickListener { movieListActivity.onItemClick(position) }
    }

    override fun getItemCount() =  movieList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvMovieTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
        var tvMovieRatings: TextView = itemView.findViewById(R.id.tv_movie_ratings)
        var tvReleaseDate: TextView = itemView.findViewById(R.id.tv_release_date)
        var ivMovieThumb: ImageView = itemView.findViewById(R.id.iv_movie_thumb)
        var pbLoadImage: ProgressBar = itemView.findViewById(R.id.pb_load_image)

    }

    override fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults? {
                val filteredResults: List<Movie?>
                if (fromDate.isEmpty() || toDate.isEmpty()) {
                    filteredResults = movieList
                } else {
                    filteredResults = getFilteredResults(fromDate, toDate)
                }
                val results = FilterResults()
                results.values = filteredResults
                return results
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                movieList = filterResults.values as List<Movie?>
                notifyDataSetChanged()
                if (itemCount == 0) {
                    movieListActivity.showEmptyView()
                } else {
                    movieListActivity.hideEmptyView()
                }
            }
        }
    }

    private fun getFilteredResults(fromDate: String, toDate: String): List<Movie?> {
        val results: MutableList<Movie> = ArrayList()
        for (item in movieList) {
            if (item?.release_date?.isEmpty()!!) {
                continue
            }
            val format: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var minDate: Date
            var maxDate: Date
            var releaseDate: Date
            try {
                minDate = format.parse(fromDate)
                maxDate = format.parse(toDate)
                releaseDate = format.parse(item.release_date)
            } catch (e: ParseException) {
                e.printStackTrace()
                continue
            }
            if (releaseDate.after(minDate) && releaseDate.before(maxDate)) {
                results.add(item)
            }
        }
        return results
    }

}
