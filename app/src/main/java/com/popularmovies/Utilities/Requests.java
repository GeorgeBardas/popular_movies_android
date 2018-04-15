package com.popularmovies.Utilities;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.popularmovies.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import org.json.JSONObject;

public class Requests {

    private Context context;
    private RequestQueue requestQueue = Volley.newRequestQueue(getInstance().context);

    private static Requests requests = new Requests();

    private Requests(){}

    public static Requests getInstance(){
        return requests;
    }

    public static Movie getMovieDetails(final Context context, int position){
        String link = context.getString(R.string.link_movie_details) + position + "?" + context.getString(R.string.end_movie_details);

        final Movie movie = new Movie();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (com.android.volley.Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        movie.setTitle(response.optString("title"));
                        movie.setImage_link(context.getString(R.string.base_path_small_poster) + response.optString("poster_path"));
                        movie.setRating(String.valueOf(response.optDouble("vote_average")));
                        movie.setReleaseDate(response.optString("release_date"));
                        movie.setOverview(response.optString("overview"));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("OBJECT", error.toString());
                    }
                });

        return null;
    }

}
