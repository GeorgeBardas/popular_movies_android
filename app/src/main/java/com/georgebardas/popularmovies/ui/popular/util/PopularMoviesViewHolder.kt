package com.georgebardas.popularmovies.ui.popular.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.popular_movie_view.view.*

class PopularMoviesViewHolder constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(R.layout.popular_movie_view, parent, false))

    companion object {
        const val MOVIE_POSTER_BASE_PATH = "https://image.tmdb.org/t/p/original/"
    }

    fun bind(movie: Movie) {
        itemView.movie_poster?.let {
            Picasso.get().load(MOVIE_POSTER_BASE_PATH + movie.poster_path).error(R.drawable.ic_dashboard_black_24dp).into(it)
        }

        itemView.movie_rating?.let {
            it.rating = movie.vote_average.toFloat() / 2
        }

        itemView.movie_rating_text?.let {
            it.text = movie.vote_average.toString()
        }

        itemView.popular_movie_title?.let {
            it.text = movie.title
        }

        itemView.movie_short_overview?.let {
            it.text = movie.overview
        }
    }

}