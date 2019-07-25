package com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.popularmovies.Utilities.DB.Data;
import com.popularmovies.Utilities.Objects.Movie;
import com.popularmovies.Utilities.Adapters.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    List<Movie> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    GridLayoutManager layoutManager;
    Data utility = Data.getInstance();
    boolean loading = false;
    int loadedPages = 1;
    HashMap<Integer, String> genres = new HashMap<>();

    @BindString(R.string.popular_link) String popularURL;
    @BindString(R.string.rated_link) String ratedURL;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.noFavoriteMoviesTV) TextView noFavoriteMoviesTV;
    @BindString(R.string.genres_link) String genresLink;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_popular:
                    displayMovies(popularURL);
                    return true;
                case R.id.navigation_rating:
                    displayMovies(ratedURL);
                    return true;
                case R.id.navigation_favorites:
                    displayFavoriteMovies();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = findViewById(R.id.recyclerView);
        movieAdapter = new MovieAdapter(movieList, R.layout.view_item_movie, context);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);

        if (savedInstanceState != null) onRestoreInstanceState(savedInstanceState);
        displayMovies(popularURL);
    }

    void displayMovies(final String url){
        noFavoriteMoviesTV.setVisibility(View.GONE);
        if (isNetworkAvailable()){
            movieList.clear();
            movieAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.VISIBLE);

            final RequestQueue requestQueue = Volley.newRequestQueue(context);

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url + "1", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray movies = response.getJSONArray("results");
                                for (int i = 0; i < 19; i++) {
                                    Movie movie = new Movie();

                                    movie.setId(movies.getJSONObject(i).optInt("id"));
                                    movie.setTitle(movies.getJSONObject(i).optString("title"));
                                    movie.setImage_link(movies.getJSONObject(i).optString("poster_path"));
                                    movie.setGenre(genres.get(movies.optJSONObject(i).optJSONArray("genre_ids").optInt(0)));
                                    movie.setRating(movies.getJSONObject(i).optString("vote_average"));

                                    movieList.add(movie);
                                    movieAdapter.notifyDataSetChanged();
                                }
                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("OBJECT", error.toString());
                        }
                    });

            JsonObjectRequest genresRequest = new JsonObjectRequest
                    (Request.Method.GET, genresLink, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray array = response.optJSONArray("genres");
                            for(int i=0;i<array.length();i++){
                                int id = array.optJSONObject(i).optInt("id");
                                String nume = array.optJSONObject(i).optString("name");
                                genres.put(id, nume);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

            requestQueue.add(genresRequest);
            requestQueue.add(jsonObjectRequest);
            loadMoreMovies(url);
        }
        else if (!isNetworkAvailable())
            Toast.makeText(context, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
    }

    void displayFavoriteMovies(){
        movieList.clear();
        progressBar.setVisibility(View.VISIBLE);
        Cursor cursor = getContentResolver().query(DetailsActivity.uri, null, null, null, null);
        while (cursor.moveToNext())
            movieList.add(new Movie().getMovieFromCursor(cursor));
        movieAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
        if (movieList.isEmpty()) noFavoriteMoviesTV.setVisibility(View.VISIBLE);
    }

    void loadMoreMovies(final String url){
        final boolean loading = false;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy > 0)
                {
                    if ((layoutManager.getChildCount() + layoutManager.findFirstVisibleItemPosition()) >= layoutManager.getItemCount() && !loading)
                    {
                        startLoading();
                        RequestQueue requestQueue = Volley.newRequestQueue(context);

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                                (Request.Method.GET, url + (++loadedPages), null, new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            JSONArray movies = response.getJSONArray("results");
                                            for (int i = 1; i <= 18; i++) {
                                                Movie movie = new Movie();

                                                movie.setId(movies.getJSONObject(i).optInt("id"));
                                                movie.setTitle(movies.getJSONObject(i).optString("title"));
                                                movie.setImage_link(movies.getJSONObject(i).optString("poster_path"));
                                                movie.setGenre(genres.get(movies.optJSONObject(i).optJSONArray("genre_ids").optInt(0)));
                                                movie.setRating(movies.getJSONObject(i).optString("vote_average"));

                                                movieList.add(movie);
                                                movieAdapter.notifyDataSetChanged();
                                            }
                                            stopLoading();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("OBJECT", error.toString());
                                    }
                                });
                        requestQueue.add(jsonObjectRequest);
                    }
                }
            }
        });
    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        utility.setMoviesList(movieList);
        outState.putInt("LIST_STATE", layoutManager.findFirstVisibleItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(context, movieList.size() + "movies", Toast.LENGTH_SHORT).show();
        movieList.clear();
        movieList.addAll(utility.getMoviesList());
        movieAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(savedInstanceState.getInt("LIST_STATE"));
    }

    void startLoading(){
        loading = true;
    }

    void stopLoading(){
        loading = false;
    }
}
