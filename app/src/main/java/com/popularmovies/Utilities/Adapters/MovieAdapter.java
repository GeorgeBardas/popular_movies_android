package com.popularmovies.Utilities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.DetailsActivity;
import com.popularmovies.MovieActivity;
import com.popularmovies.R;
import com.popularmovies.Utilities.Objects.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter{

    List<Movie> movieList;
    int resourceID;
    Context context;
    Movie movie;

    @BindColor(R.color.rating1) int rating1;
    @BindColor(R.color.rating2) int rating2;
    @BindColor(R.color.rating3) int rating3;
    @BindColor(R.color.rating4) int rating4;
    @BindDrawable(R.drawable.rounded_view) Drawable drawable;

    public MovieAdapter(List<Movie> movieList, int resourceID, Context context) {
        this.movieList = movieList;
        this.resourceID = resourceID;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resourceID, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ButterKnife.bind(this, holder.itemView);
        ImageView imageMovie = holder.itemView.findViewById(R.id.movie_image);
        TextView genre = holder.itemView.findViewById(R.id.genre);
        ConstraintLayout textbar = holder.itemView.findViewById(R.id.text_bar);

        Picasso.with(context).load(context.getString(R.string.base_path_small_poster) + movieList.get(position).getImage_link()).error(R.drawable.ic_android).into(imageMovie);
        genre.setText(movieList.get(position).getGenre());

        Float rating = Float.valueOf(movieList.get(position).getRating());
        if (rating < 4) drawable.setTint(rating1);
        else if (rating >= 4 && rating < 6) drawable.setTint(rating2);
            else if (rating >= 6 && rating < 7) drawable.setTint(rating3);
                else drawable.setTint(rating4);

        if (Float.valueOf(movieList.get(position).getRating()) < 8) textbar.setBackground(drawable);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieActivity.class);
                intent.putExtra("id", movieList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{

        ImageView imageMovie;
        TextView genre;
        ConstraintLayout textBar;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.movie_image);
            genre = itemView.findViewById(R.id.genre);
            textBar = itemView.findViewById(R.id.text_bar);
        }
    }
}
