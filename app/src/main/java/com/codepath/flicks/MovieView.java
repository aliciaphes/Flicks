package com.codepath.flicks;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Alicia on 15-Oct-16.
 */

public class MovieView extends RelativeLayout{

    private ImageView ivPicture;
    private TextView tvTitle;
    private TextView tvOverview;

    public MovieView(Context c) {
        this(c, null);
    }

    public MovieView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovieView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //LayoutInflater.from(context).inflate(R.layout.item_movie2, this, true);
        LayoutInflater.from(context).inflate(R.layout.item_movie, this, true);
        setupChildren();
    }


    private void setupChildren() {
        ivPicture = (ImageView) findViewById(R.id.ivMovieImage);
        tvTitle= (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
    }


    public static MovieView inflate(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MovieView movieView = (MovieView) inflater.inflate(R.layout.movie_view, parent, false);
        return movieView;
    }

    public void setItem(Movie movie) {

        //determine which image to load depending on screen orientation:
        String imageToLoad = null;
        int orientation = getContext().getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            imageToLoad = movie.getPosterPath();
        }else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            imageToLoad = movie.getBackdropPath();
        }
        if(imageToLoad != null) {
            Picasso.with(getContext()).load(imageToLoad)
                    //.fit().centerCrop()
                    .transform(new RoundedCornersTransformation(20, 20))
                    .placeholder(R.drawable.placeholder)
                    .into(ivPicture);
        }

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());
    }

}
