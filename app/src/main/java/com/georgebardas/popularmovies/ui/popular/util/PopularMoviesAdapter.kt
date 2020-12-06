package com.georgebardas.popularmovies.ui.popular.util

import com.georgebardas.popularmovies.ui.recent.util.RandomMovieViewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.random_movie_view.view.*

class PopularMoviesAdapter : RecyclerView.Adapter<PopularMoviesViewHolder>() {
    var list: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        return PopularMoviesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setItem(list: MutableList<Movie>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}