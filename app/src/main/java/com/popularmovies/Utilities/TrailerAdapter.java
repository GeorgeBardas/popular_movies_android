package com.popularmovies.Utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.popularmovies.R;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {

    List<Trailer> list;
    View trailerView;

    public TrailerAdapter(@NonNull Context context, int res, @NonNull List<Trailer> objects) {
        super(context, res, objects);
        list = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.view_item_trailer, null);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + list.get(position).getLink()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + list.get(position).getLink()));
                try {
                    getContext().startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    getContext().startActivity(webIntent);
                }
            }
        });

        TextView title = view.findViewById(R.id.trailer_title);
        title.setText(list.get(position).getName());

        return view;
    }
}
