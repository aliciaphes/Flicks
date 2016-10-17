package com.codepath.flicks.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.flicks.MovieView;
import com.codepath.flicks.models.Movie;

import java.util.List;

/**
 * Created by Alicia P on 13-Oct-16.
 */


public class MovieArrayAdapter extends ArrayAdapter<Movie>{

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //use of subclassing to display every movie as part of the listView
        Movie movie = getItem(position);
        MovieView movieView = (MovieView)convertView;
        if(movieView == null){
            movieView = MovieView.inflate(parent);
        }
        movieView.setItem(movie);
        return movieView;
    }

}
