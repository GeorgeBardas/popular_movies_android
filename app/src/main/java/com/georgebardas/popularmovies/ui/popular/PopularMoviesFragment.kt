package com.georgebardas.popularmovies.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.ui.popular.util.PopularMoviesAdapter
import kotlinx.android.synthetic.main.fragment_popular_movies.*

class PopularMoviesFragment : Fragment() {

    private lateinit var popularMoviesViewModel: PopularMoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        popularMoviesViewModel =
            ViewModelProvider(this).get(PopularMoviesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_popular_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popular_movies_list?.layoutManager = LinearLayoutManager(context)
        popular_movies_list?.adapter = PopularMoviesAdapter()

        popularMoviesViewModel.getPopularMovies(context)

        popularMoviesViewModel.moviesResponse.observe(viewLifecycleOwner, Observer {
            (popular_movies_list?.adapter as? PopularMoviesAdapter)?.setItem(it)
        })
    }
}