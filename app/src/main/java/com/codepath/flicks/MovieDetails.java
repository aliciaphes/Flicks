package com.codepath.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetails extends AppCompatActivity {

    TextView tvTitle;
    TextView tvReleaseDate;
    RatingBar ratingBar;
    TextView tvSynopsis;
    ImageView ivImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        //retrieve movie that's been 'sent' from main activity
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");


        //retrieve all fields and set their value
        tvTitle = ButterKnife.findById(this, R.id.title);
        tvTitle.setText(movie.getOriginalTitle());

        tvReleaseDate = ButterKnife.findById(this, R.id.release_date);
        tvReleaseDate.setText("Release date: " + movie.getReleaseDate());

        tvSynopsis = ButterKnife.findById(this, R.id.synopsis);
        tvSynopsis.setText(movie.getOverview());

        ratingBar = ButterKnife.findById(this, R.id.rating_bar);
        ratingBar.setRating((float)movie.getRating());

        ivImage = ButterKnife.findById(this, R.id.ivMovieImage);
        Picasso.with(this).load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(20, 20))
                .placeholder(R.drawable.placeholder)
                .into(ivImage);
    }


}
