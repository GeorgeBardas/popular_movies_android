package com.georgebardas.popularmovies.ui.recent

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.ui.recent.util.RandomMovieAdapter
import kotlinx.android.synthetic.main.fragment_latest_movies.*
import java.lang.Math.abs

class RecentMoviesFragment : Fragment() {

    private lateinit var homeViewModel: RecentMoviesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =  ViewModelProvider(this).get(RecentMoviesViewModel::class.java)
        return inflater.inflate(R.layout.fragment_latest_movies, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.moviesResponse.observe(viewLifecycleOwner, Observer {
            (view_pager?.adapter as? RandomMovieAdapter)?.setItem(it)
        })

        homeViewModel.getPopularMovies(context)

        view.findViewById<ViewPager2>(R.id.view_pager).adapter =
            RandomMovieAdapter()
        view.findViewById<ViewPager2>(R.id.view_pager)?.offscreenPageLimit = 1

        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            page.scaleY = 1 - (0.25f * abs(position))
            page.alpha = 0.25f + (1 - abs(position))
        }
        view.findViewById<ViewPager2>(R.id.view_pager).setPageTransformer(pageTransformer)

        context?.let {
            val itemDecoration = HorizontalMarginItemDecoration(
                it,
                R.dimen.viewpager_current_item_horizontal_margin
            )
            view.findViewById<ViewPager2>(R.id.view_pager).addItemDecoration(itemDecoration)
        }
    }

    inner class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
        RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }

    }
}