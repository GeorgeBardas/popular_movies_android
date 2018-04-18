package com.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.popularmovies.Utilities.Movie;
import com.popularmovies.Utilities.MovieDatabaseContentProvider;
import com.popularmovies.Utilities.MovieDatabaseHelper;
import com.popularmovies.Utilities.MovieTable;
import com.popularmovies.Utilities.MoviesDatabase;
import com.popularmovies.Utilities.Trailer;
import com.popularmovies.Utilities.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.popularmovies.MainActivity.context;

public class DetailsActivity extends AppCompatActivity {

    Movie movie;
    List<Trailer> listTrailers = new ArrayList<>();
    List<String> reviews = new ArrayList<>();
    public static Uri uri = Uri.parse(MovieDatabaseContentProvider.BASE_PATH + MovieDatabaseContentProvider.PACKAGE + "/" + MovieTable.TABLE_MOVIE);

    @BindView(R.id.trailers_list_view) ListView trailersListView;
    @BindView(R.id.reviews_list_view) ListView reviewsListView;
    @BindView(R.id.movie_image) ImageView movieImage;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_release_date) TextView movieReleaseDate;
    @BindView(R.id.movie_rating) TextView movieRating;
    @BindView(R.id.movie_plot) TextView moviePlot;
    @BindView(R.id.favorite_button) Button favorite;

    public boolean isUnique(){
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        while (cursor.moveToNext())
            if (cursor.getString(cursor.getColumnIndex("title")).equals(movie.getTitle()))
                return false;
        cursor.close();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movie = new Movie();

        final TrailerAdapter trailerAdapter = new TrailerAdapter(getApplicationContext(), R.layout.view_item_trailer, listTrailers);
        trailersListView.setAdapter(trailerAdapter);
        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reviews);
        reviewsListView.setAdapter(adapter);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int position = getIntent().getIntExtra("id", 0);
        String link = getString(R.string.link_movie_details) + position + "?" + getString(R.string.end_movie_details);
        final String trailers = getString(R.string.link_movie_details) + position + "/videos?" + getString(R.string.end_movie_details);
        final String reviewsLink = getString(R.string.link_movie_details) + position + "/reviews?" + getString(R.string.end_movie_details);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movie.setId(response.optInt("id"));
                        movie.setTitle(response.optString("title"));
                        movie.setImage_link(response.optString("poster_path"));
                        movie.setOverview(response.optString("overview"));
                        movie.setReleaseDate(response.optString("release_date"));
                        movie.setRating(String.valueOf(response.optDouble("vote_average")));
                        toolbar.setTitle(response.optString("title"));
                        Picasso.with(getApplicationContext()).load(getString(R.string.base_path_small_poster) + response.optString("poster_path")).error(R.drawable.ic_android).into(movieImage);
                        movieTitle.setText(response.optString("title"));
                        movieReleaseDate.setText("Release date: " + response.optString("release_date"));
                        movieRating.setText("Rating: " + String.valueOf(response.optDouble("vote_average")));
                        moviePlot.setText(response.optString("overview"));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("OBJECT", error.toString());
                    }
                });

        JsonObjectRequest trailersRequest = new JsonObjectRequest(Request.Method.GET, trailers, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray results = response.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        JSONObject object = results.getJSONObject(i);
                        Trailer trailer = new Trailer(object.optString("name"), object.optString("key"));
                        listTrailers.add(trailer);
                        trailerAdapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        JsonObjectRequest reviewsRequest = new JsonObjectRequest(Request.Method.GET, reviewsLink, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray results = response.getJSONArray("results");
                    for(int i=0;i<results.length();i++){
                        reviews.add(results.getJSONObject(i).optString("content"));
                        adapter.notifyDataSetChanged();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);
        requestQueue.add(trailersRequest);
        requestQueue.add(reviewsRequest);

            favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUnique()) {
                    getContentResolver().insert(uri, movie.getContentValues());
                    Toast.makeText(DetailsActivity.this, movie.getTitle() + " aded to favorites", Toast.LENGTH_SHORT).show();
                }
                else {
                    getContentResolver().delete(uri, MovieTable.COLUMN_TITLE + "= ?", new String[] {movie.getTitle()});
                    Toast.makeText(DetailsActivity.this, "Deleted from favorites", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
