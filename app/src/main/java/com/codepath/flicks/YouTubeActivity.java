package com.codepath.flicks;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.flicks.utils.Utils;
import com.codepath.flicks.models.Movie;
import com.codepath.flicks.models.Trailer;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


//This link includes how to register my app in Google's developer console:
//https://www.sitepoint.com/using-the-youtube-api-to-embed-video-in-an-android-app/

public class YouTubeActivity extends YouTubeBaseActivity {

    private ArrayList<Trailer> trailers;
    private Movie movie;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_youtube);

        //retrieve movie that's been 'sent' from main activity
        movie = (Movie) getIntent().getSerializableExtra("movie");

        //retrieve ID of movie
        int movieID = movie.getID();

        trailers = new ArrayList<>();

        String url = Utils.getBaseURL() + Integer.toString(movieID) + "/videos?api_key=" + Utils.getMovieDBAPIkey();

        fetchMovieVideos(url);

    }


    private void fetchMovieVideos(String url) {
        //make sure there's access to the web
        boolean connectivity = Utils.checkForConnectivity(this);

        if (!connectivity) {
            Toast.makeText(this, "Unable to continue, no connection detected", Toast.LENGTH_LONG).show();
        } else {
            Utils.getClient().get(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray trailersJsonResults = null;

                    try {
                        trailersJsonResults = response.getJSONArray("results");
                        trailers.addAll(Trailer.fromJSONArray(trailersJsonResults));
                        setUpLayout();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }

    private void setUpLayout() {

        TextView tvTitle = ButterKnife.findById(this, R.id.tv_title);
        tvTitle.setText(movie.getOriginalTitle());

        setUpVideoPlayer();
    }

    private void setUpVideoPlayer() {
        //trailers contains now all the trailers.
        //Let's randomly select the first one that is type 'Trailer'
        String selected = null;
        Trailer trailer;
        for (int i = 0; i < trailers.size() && selected == null; i++) {
            trailer = trailers.get(i);
            if (trailer.getTrailerType().equals("Trailer")) {
                selected = trailer.getTrailerID();
            }
        }

        final String trailerID = selected;
        if (trailerID != null) {

            YouTubePlayerView youTubePlayerView =
                    (YouTubePlayerView) findViewById(R.id.youtube_player);

            youTubePlayerView.initialize(Utils.getYouTubeAPIkey(),
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.setFullscreen(true);
                            youTubePlayer.loadVideo(trailerID);
                            // or to not play immediately call cueVideo instead
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
        }

    }
}
