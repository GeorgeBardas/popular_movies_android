package com.popularmovies.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.DetailsActivity;
import com.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends ArrayAdapter<Movie> {

    List<Movie> movieList;
    int resourceID;

    public MovieAdapter(@NonNull Context context, int resource, List<Movie> movieList) {
        super(context, resource, movieList);
        this.movieList = movieList;
        this.resourceID = resource;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View layout = view;

        if (layout == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            layout = inflater.inflate(resourceID, null);
        }

        ImageView imageMovie = layout.findViewById(R.id.movie_image);
        TextView titleMovie = layout.findViewById(R.id.movie_title);

        final Movie movie = movieList.get(i);
        Picasso.with(getContext()).load(getContext().getString(R.string.base_path_small_poster) + movie.getImage_link()).error(R.drawable.ic_android).into(imageMovie);
        titleMovie.setText(movie.getTitle());

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("id", movie.getId());
                getContext().startActivity(intent);
            }
        });

        return layout;
    }
}
