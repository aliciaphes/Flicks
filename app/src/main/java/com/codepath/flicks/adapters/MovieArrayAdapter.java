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
        //super(context, android.R.layout.simple_list_item_1, movies);
        super(context, 0, movies);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //convertView = before(position, convertView, parent);
        //return convertView;

        //use of subclassing to display every movie as part of the listView
        MovieView movieView = (MovieView)convertView;
        if(movieView == null){
            movieView = MovieView.inflate(parent);
        }
        movieView.setItem(getItem(position));
        return movieView;
    }



//    private View before(int position, View convertView, ViewGroup parent) {
//        //get the data item for position
//        Movie movie = getItem(position);
//
//        //check the existing view being reused
//        if (convertView == null) {
//            // If there's no view to re-use, inflate a brand new view for row
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.item_movie, parent, false);
//        }
//
//        //find the image view
//        ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
//        //clear out image from convertView
//        ivImage.setImageResource(0);
//
//        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
//        TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
//
//        //populate data
//        tvTitle.setText(movie.getOriginalTitle());
//        tvOverview.setText(movie.getOverview());
//
//        //determine which image to load depending on screen orientation:
//        String imageToLoad = null;
//        int orientation = getContext().getResources().getConfiguration().orientation;
//        if(orientation == Configuration.ORIENTATION_PORTRAIT){
//            imageToLoad = movie.getPosterPath();
//        }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
//            imageToLoad = movie.getBackdropPath();
//        }
//        if(imageToLoad != null) {
//            Picasso.with(getContext()).load(imageToLoad)
//                    .transform(new RoundedCornersTransformation(20, 20))
//                    .placeholder(R.drawable.placeholder)
//                    .into(ivImage);
//        }
//        //return the view
//        return convertView;
//    }

}
