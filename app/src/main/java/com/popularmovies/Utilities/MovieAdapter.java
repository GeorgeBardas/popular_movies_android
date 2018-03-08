package com.popularmovies.Utilities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.DetailsActivity;
import com.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter{

    List<Movie> movieList;
    int resourceID;
    Context context;
    Movie movie;

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
        ImageView imageMovie = holder.itemView.findViewById(R.id.movie_image);
        Picasso.with(context).load(context.getString(R.string.base_path_small_poster) + movieList.get(position).getImage_link()).error(R.drawable.ic_android).into(imageMovie);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
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
        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageMovie = itemView.findViewById(R.id.movie_image);
        }
    }
}
