package com.georgebardas.popularmovies.ui.popular

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.georgebardas.popularmovies.R
import com.georgebardas.popularmovies.model.GetMoviesResponse
import com.georgebardas.popularmovies.model.Movie
import com.georgebardas.popularmovies.util.RetrofitClientInstance
import com.georgebardas.popularmovies.util.TmdbService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PopularMoviesViewModel : ViewModel() {

    val moviesResponse = MutableLiveData<MutableList<Movie>>()
    val request = RetrofitClientInstance.buildService(TmdbService::class.java)

    fun getPopularMovies(context: Context?, isPopular: Boolean) {
        context?.let {
            val call = when (isPopular){
                true -> request.getPopularMovies(context.getString(R.string.api_key))
                false -> request.getTopRatedMovies(context.getString(R.string.api_key))
            }

            call.enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(call: Call<GetMoviesResponse>, response: Response<GetMoviesResponse>) {
                    if (response.isSuccessful){
                        response.body()?.results?.let {
                            moviesResponse.value = it.toMutableList()
                        }
//                    progress_bar.visibility = View.GONE
//                    recyclerView.apply {
//                        setHasFixedSize(true)
//                        layoutManager = LinearLayoutManager(this@MainActivity)
//                        adapter = MoviesAdapter(response.body()!!.results)
//                    }
                    }
                }
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}