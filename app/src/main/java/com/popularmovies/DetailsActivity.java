package com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.popularmovies.Utilities.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.popularmovies.MainActivity.context;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_image) ImageView movieImage;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_release_date) TextView movieReleaseDate;
    @BindView(R.id.movie_rating) TextView movieRating;
    @BindView(R.id.movie_plot) TextView moviePlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int position = getIntent().getIntExtra("id", 0);
        String link = getString(R.string.link_movie_details) + position + "?" + getString(R.string.end_movie_details);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
        requestQueue.add(jsonObjectRequest);

    }
}
