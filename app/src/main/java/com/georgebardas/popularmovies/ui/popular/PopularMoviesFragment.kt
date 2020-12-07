package com.georgebardas.popularmovies.ui.popular

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.ui.popular.util.PopularMoviesAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_popular_movies.*
import kotlinx.android.synthetic.main.fragment_popular_movies.view.*

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
        popular_movies_list?.adapter = PopularMoviesAdapter { movieId ->
            view.findNavController().navigate(R.id.action_navigation_popular_movies_to_movieDetailsFragment, bundleOf("movieId" to movieId))
        }

        popularMoviesViewModel.getPopularMovies(context, isPopular = true)

        popularMoviesViewModel.moviesResponse.observe(viewLifecycleOwner, Observer {
            (popular_movies_list?.adapter as? PopularMoviesAdapter)?.setItem(it)
        })

        view.movies_tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> popularMoviesViewModel.getPopularMovies(context, isPopular = true)
                    1 -> popularMoviesViewModel.getPopularMovies(context, isPopular = false)
                }
            }
        })
    }
}