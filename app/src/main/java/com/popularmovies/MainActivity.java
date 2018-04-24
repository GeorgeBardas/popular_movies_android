package com.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.popularmovies.Utilities.Data;
import com.popularmovies.Utilities.Movie;
import com.popularmovies.Utilities.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    List<Movie> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    MovieAdapter movieAdapter;
    GridLayoutManager layoutManager;
    Parcelable state;
    boolean savedInstace = false;
    Data utility = Data.getInstance();

    @BindString(R.string.popular_link) String popularURL;
    @BindString(R.string.rated_link) String ratedURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        recyclerView = findViewById(R.id.recyclerView);
        movieAdapter = new MovieAdapter(movieList, R.layout.view_item_movie, context);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(movieAdapter);

        if (savedInstanceState != null) onRestoreInstanceState(savedInstanceState);
        displayMovies(popularURL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listing_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.popular:
                displayMovies(popularURL);
                break;

            case R.id.rating:
                displayMovies(ratedURL);
                break;

            case R.id.favorite:
                displayFavoriteMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    void displayMovies(String url){
        if (isNetworkAvailable() && movieList.isEmpty()){
            movieList.clear();

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray movies = response.getJSONArray("results");
                                for (int i = 1; i < 20; i++) {
                                    Movie movie = new Movie();
                                    movie.setId(movies.getJSONObject(i).optInt("id"));
                                    movie.setTitle(movies.getJSONObject(i).optString("title"));
                                    movie.setImage_link(movies.getJSONObject(i).optString("poster_path"));
                                    movieList.add(movie);
                                    movieAdapter.notifyDataSetChanged();
                                }
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
        else if (!isNetworkAvailable())
            Toast.makeText(context, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
    }

    void displayFavoriteMovies(){
        movieList.clear();
        Cursor cursor = getContentResolver().query(DetailsActivity.uri, null, null, null, null);
        while (cursor.moveToNext())
            movieList.add(new Movie().getMovieFromCursor(cursor));
        movieAdapter.notifyDataSetChanged();
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
}
