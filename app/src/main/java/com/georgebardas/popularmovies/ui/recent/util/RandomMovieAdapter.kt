package com.georgebardas.popularmovies.ui.recent.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.random_movie_view.view.*

class RandomMovieAdapter : RecyclerView.Adapter<RandomMovieViewHolder>() {
    var list: MutableList<Movie> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomMovieViewHolder {
        return RandomMovieViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RandomMovieViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun setItem(list: MutableList<Movie>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size
}