package com.codepath.flicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.codepath.flicks.R.id.ivMovieImage;

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
        final Movie movie = (Movie) getIntent().getSerializableExtra("movie");


        //retrieve all fields and set their value
        tvTitle = ButterKnife.findById(this, R.id.title);
        tvTitle.setText(movie.getOriginalTitle());

        tvReleaseDate = ButterKnife.findById(this, R.id.release_date);
        tvReleaseDate.setText("Release date: " + movie.getReleaseDate());

        tvSynopsis = ButterKnife.findById(this, R.id.synopsis);
        tvSynopsis.setText(movie.getOverview());

        ratingBar = ButterKnife.findById(this, R.id.rating_bar);
        ratingBar.setRating((float) movie.getRating());

        ivImage = ButterKnife.findById(this, ivMovieImage);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                //launch video activity
                intent = new Intent(MovieDetails.this, YouTubeActivity.class);

                if (intent != null) {
                    // put movie as "extra" into the bundle for access in YouTubeActivity
                    intent.putExtra("movie", movie);
                    startActivity(intent);
                }

            }
        });

        Picasso.with(this).load(movie.getBackdropPath())
                .transform(new RoundedCornersTransformation(20, 20))
                .placeholder(R.drawable.placeholder)
                .into(ivImage);

    }


}
