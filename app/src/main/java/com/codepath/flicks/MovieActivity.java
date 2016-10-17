package com.codepath.flicks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.flicks.adapters.MovieArrayAdapter;
import com.codepath.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {

    private ArrayList<Movie> movies;
    private MovieArrayAdapter movieAdapter;
    private ListView lvMovies;
    private SwipeRefreshLayout swipeContainer;
    private final AsyncHttpClient client = new AsyncHttpClient();
    private final String URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //set up adapter
        lvMovies = ButterKnife.findById(this, R.id.lvMovies);
        movies = new ArrayList<>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvMovies.setAdapter(movieAdapter);

        // setup refresh listener which triggers new data loading
        swipeContainer = ButterKnife.findById(this, R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

            @Override
            public void onRefresh() {
                //CLEAR OUT old items before appending in the new ones:
                movieAdapter.clear();
                movies.clear();
                movieAdapter.notifyDataSetChanged();

                fetchData(client);
                //fetchHardcodedData();

                //signal refresh has finished:
                swipeContainer.setRefreshing(false);
            }
        });


        //fetchHardcodedData();
        fetchData(client);

        setUpClickListener();

    }

    private void setUpClickListener() {
        lvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //view is an instance of MovieView
                //Expose details of movie (ratings (out of 10), popularity, and synopsis
                //ratings using RatingBar
                Intent detailsIntent = new Intent(MovieActivity.this, MovieDetails.class);

                // put movie as "extra" into the bundle for access in the second activity
                detailsIntent.putExtra("movie", movies.get(position));
                // bring up the second activity
                startActivity(detailsIntent);
            }
        });
    }


    private void fetchData(AsyncHttpClient client) {

        //make sure there's access to the web
        boolean connectivity = checkForConnectivity();

        if(!connectivity){
            Toast.makeText(this, "Unable to continue, no connection detected", Toast.LENGTH_LONG).show();
        }

        else{
            client.get(URL, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray movieJsonResults = null;

                    try {
                        movieJsonResults = response.getJSONArray("results");
                        movies.addAll(Movie.fromJSONArray(movieJsonResults));
                        movieAdapter.notifyDataSetChanged();

                        //signal refresh has finished:
                        swipeContainer.setRefreshing(false);

                        Log.d("DEBUG", movies.toString());
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

    private boolean checkForConnectivity() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


//    private void fetchHardcodedData() throws JSONException {
//
//        JSONObject response = new JSONObject("{\"page\":1,\"results\":[{\"poster_path\":\"\\/z6BP8yLwck8mN9dtdYKkZ4XGa3D.jpg\",\"adult\":false,\"overview\":\"A big screen remake of John Sturges' classic western The Magnificent Seven, itself a remake of Akira Kurosawa's Seven Samurai. Seven gun men in the old west gradually come together to help a poor village against savage thieves.\",\"release_date\":\"2016-09-14\",\"genre_ids\":[28,12,37],\"id\":333484,\"original_title\":\"The Magnificent Seven\",\"original_language\":\"en\",\"title\":\"The Magnificent Seven\",\"backdrop_path\":\"\\/T3LrH6bnV74llVbFpQsCBrGaU9.jpg\",\"popularity\":28.566484,\"vote_count\":509,\"video\":false,\"vote_average\":4.57},{\"poster_path\":\"\\/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg\",\"adult\":false,\"overview\":\"A teenager finds himself transported to an island where he must help protect a group of orphans with special powers from creatures intent on destroying them.\",\"release_date\":\"2016-09-28\",\"genre_ids\":[14],\"id\":283366,\"original_title\":\"Miss Peregrine's Home for Peculiar Children\",\"original_language\":\"en\",\"title\":\"Miss Peregrine's Home for Peculiar Children\",\"backdrop_path\":\"\\/qXQinDhDZkTiqEGLnav0h1YSUu8.jpg\",\"popularity\":21.385542,\"vote_count\":185,\"video\":false,\"vote_average\":6.1},{\"poster_path\":\"\\/oFOG2yIRcluKfTtYbzz71Vj9bgz.jpg\",\"adult\":false,\"overview\":\"After waking up in a hospital with amnesia, professor Robert Langdon and a doctor must race against time to foil a deadly global plot.\",\"release_date\":\"2016-10-13\",\"genre_ids\":[28,80,9648,53],\"id\":207932,\"original_title\":\"Inferno\",\"original_language\":\"en\",\"title\":\"Inferno\",\"backdrop_path\":\"\\/mL1hZ8Z7L1Px8tIzalnYKKdhUqo.jpg\",\"popularity\":14.138574,\"vote_count\":67,\"video\":false,\"vote_average\":4.39},{\"poster_path\":\"\\/7D6hM7IR0TbQmNvSZVtEiPM3H5h.jpg\",\"adult\":false,\"overview\":\"A story set on the offshore drilling rig Deepwater Horizon, which exploded during April 2010 and created the worst oil spill in U.S. history.\",\"release_date\":\"2016-09-28\",\"genre_ids\":[18],\"id\":296524,\"original_title\":\"Deepwater Horizon\",\"original_language\":\"en\",\"title\":\"Deepwater Horizon\",\"backdrop_path\":\"\\/zjYdnBHbIOYBqKZxvBUsT5MevUA.jpg\",\"popularity\":13.366875,\"vote_count\":179,\"video\":false,\"vote_average\":4.45},{\"poster_path\":\"\\/pFgvoEaiXxgw5GIg2vIxPDYX606.jpg\",\"adult\":false,\"overview\":\"Rachel Watson, an alcoholic who divorced her husband Tom after she caught him cheating on her, takes the train to work daily. She fantasizes about the relationship of her neighbours, Scott and Megan Hipwell, during her commute. That all changes when she witnesses something from the train window and Megan is missing, presumed dead.\",\"release_date\":\"2016-10-06\",\"genre_ids\":[53],\"id\":346685,\"original_title\":\"The Girl on the Train\",\"original_language\":\"en\",\"title\":\"The Girl on the Train\",\"backdrop_path\":\"\\/fpq86AP0YBYUwNgDvUj5kxwycxH.jpg\",\"popularity\":7.761346,\"vote_count\":89,\"video\":false,\"vote_average\":4.97},{\"poster_path\":\"\\/l9BWPqUV57X5ELBDLlbO7Vhh3Mr.jpg\",\"adult\":false,\"overview\":\"As a math savant uncooks the books for a new client, the Treasury Department closes in on his activities and the body count starts to rise.\",\"release_date\":\"2016-10-14\",\"genre_ids\":[80,53,18],\"id\":302946,\"original_title\":\"The Accountant\",\"original_language\":\"en\",\"title\":\"The Accountant\",\"backdrop_path\":\"\\/i9flZtw3BwukADQpu5PlrkwPYSY.jpg\",\"popularity\":6.739663,\"vote_count\":27,\"video\":false,\"vote_average\":1.96},{\"poster_path\":\"\\/IfB9hy4JH1eH6HEfIgIGORXi5h.jpg\",\"adult\":false,\"overview\":\"Jack Reacher returns to the headquarters of his old unit, only to find out he's now accused of a 16-year-old homicide.\",\"release_date\":\"2016-10-19\",\"genre_ids\":[53,28,80,18,9648],\"id\":343611,\"original_title\":\"Jack Reacher: Never Go Back\",\"original_language\":\"en\",\"title\":\"Jack Reacher: Never Go Back\",\"backdrop_path\":\"\\/4ynQYtSEuU5hyipcGkfD6ncwtwz.jpg\",\"popularity\":5.81558,\"vote_count\":34,\"video\":false,\"vote_average\":5.63},{\"poster_path\":\"\\/inFoGkUfzg56iuTBVDifzTq7jVx.jpg\",\"adult\":false,\"overview\":\"The adventures of teenager Max McGrath and alien companion Steel, who must harness and combine their tremendous new powers to evolve into the turbo-charged superhero Max Steel.\",\"release_date\":\"2016-10-14\",\"genre_ids\":[878,28,12],\"id\":286567,\"original_title\":\"Max Steel\",\"original_language\":\"en\",\"title\":\"Max Steel\",\"backdrop_path\":\"\\/41nLaq19eZeXWFqRaYNtzHjaaH.jpg\",\"popularity\":5.080999,\"vote_count\":19,\"video\":false,\"vote_average\":7.3},{\"poster_path\":\"\\/aFy0ZiUnGNu4SHjZwXX0EpG6cFv.jpg\",\"adult\":false,\"overview\":\"An indigenous family, one of the last forest people of old, copes with the unusual changes in their environment including the imposing lifestyle and customs of the more sociable tribes on the plains.\",\"release_date\":\"2016-10-14\",\"genre_ids\":[12,18,14],\"id\":420901,\"original_title\":\"Baboy Halas\",\"original_language\":\"en\",\"title\":\"Baboy Halas\",\"backdrop_path\":null,\"popularity\":4.42915,\"vote_count\":0,\"video\":false,\"vote_average\":0},{\"poster_path\":\"\\/5qVD5TD1CiALR5vUsMzh2BschVU.jpg\",\"adult\":false,\"overview\":\"Storks deliver babies…or at least they used to. Now they deliver packages for a global internet retail giant. Junior, the company’s top delivery stork, is about to be promoted when he accidentally activates the Baby Making Machine, producing an adorable and wholly unauthorized baby girl...\",\"release_date\":\"2016-09-22\",\"genre_ids\":[16,35,10751],\"id\":332210,\"original_title\":\"Storks\",\"original_language\":\"en\",\"title\":\"Storks\",\"backdrop_path\":\"\\/qvFYOUo072ZyhVoNSQdFqlq7uGD.jpg\",\"popularity\":4.320696,\"vote_count\":46,\"video\":false,\"vote_average\":4.71},{\"poster_path\":\"\\/yAdwXn2SsbJvkP42ZVEGoayJ0nW.jpg\",\"adult\":false,\"overview\":\"Lovable and friendly, the trolls love to play around. But one day, a mysterious giant shows up to end the party. Poppy, the optimistic leader of the Trolls, and her polar opposite, Branch, must embark on an adventure that takes them far beyond the only world they’ve ever known.\",\"release_date\":\"2016-10-13\",\"genre_ids\":[10751,16,12],\"id\":136799,\"original_title\":\"Trolls\",\"original_language\":\"en\",\"title\":\"Trolls\",\"backdrop_path\":\"\\/wc1JxADaBLuWhySkaawCBTpixCo.jpg\",\"popularity\":4.278979,\"vote_count\":7,\"video\":false,\"vote_average\":0},{\"poster_path\":\"\\/4ISlMtmW1xNk6poAEzppbHjqRVO.jpg\",\"adult\":false,\"overview\":\"A boy attempts to deal with his mother's illness and the bullying of his classmates by escaping to a fantastical world.\",\"release_date\":\"2016-10-07\",\"genre_ids\":[14,18],\"id\":258230,\"original_title\":\"A Monster Calls\",\"original_language\":\"en\",\"title\":\"A Monster Calls\",\"backdrop_path\":\"\\/xVW8REyVqKwxAtUYY07UGlZH43L.jpg\",\"popularity\":4.182924,\"vote_count\":33,\"video\":false,\"vote_average\":7.12},{\"poster_path\":\"\\/gJlv7V5nxiNyd2mfekdXloCrPk4.jpg\",\"adult\":false,\"overview\":\"A woman returns to the village of Kagbunga in the Bikol region carrying the ashes of her only son via the old train that circles her universe like the tandayag, the primordial serpent. In a filial act of mourning, she will reckon and reconcile the thin line that exists between tragedy and transcendence and prove and that even the most broken life can be restored to its moments.\",\"release_date\":\"2016-10-14\",\"genre_ids\":[18],\"id\":420941,\"original_title\":\"Hinulid\",\"original_language\":\"en\",\"title\":\"Hinulid\",\"backdrop_path\":null,\"popularity\":4.12915,\"vote_count\":0,\"video\":false,\"vote_average\":0},{\"poster_path\":\"\\/4Yhxv6omziQCJks1gZ2Wuhhb3qq.jpg\",\"adult\":false,\"overview\":\"Based on the book by Megan Shull, The Swap follows the adventures of a Rhythmic Gymnast named Ellie with a Make-it-or-Break-it Competition, and the younger brother named Jack in a Hockey Family, who's vying for a varsity spot on his school team. But when a Simple Text causes the two to Swap Bodies, their paths take an Unexpected Cross.\",\"release_date\":\"2016-10-07\",\"genre_ids\":[35,10770],\"id\":417028,\"original_title\":\"The Swap\",\"original_language\":\"en\",\"title\":\"The Swap\",\"backdrop_path\":\"\\/hZOTNJPmuAg5pZYHyF2GcHlA6Y2.jpg\",\"popularity\":3.948631,\"vote_count\":2,\"video\":false,\"vote_average\":8},{\"poster_path\":\"\\/j8di6S3mUuFe5Yz8etRG8yG5EeE.jpg\",\"adult\":false,\"overview\":\"Breaking up with Mark Darcy leaves Bridget Jones over 40 and single again. Feeling that she has everything under control, Jones decides to focus on her career as a top news producer. Suddenly, her love life comes back from the dead when she meets a dashing and handsome American named Jack. Things couldn't be better, until Bridget discovers that she is pregnant. Now, the befuddled mom-to-be must figure out if the proud papa is Mark or Jack.\",\"release_date\":\"2016-09-15\",\"genre_ids\":[35,10749],\"id\":95610,\"original_title\":\"Bridget Jones's Baby\",\"original_language\":\"en\",\"title\":\"Bridget Jones's Baby\",\"backdrop_path\":\"\\/u81y11sFzOIHdduSrrajeHOaCbU.jpg\",\"popularity\":3.934474,\"vote_count\":148,\"video\":false,\"vote_average\":6.29},{\"poster_path\":\"\\/GW3IyMW5Xgl0cgCN8wu96IlNpD.jpg\",\"adult\":false,\"overview\":\"Adam West and Burt Ward returns to their iconic roles of Batman and Robin. Featuring the voices of Adam West, Burt Ward, and Julie Newmar, the film sees the superheroes going up against classic villains like The Joker, The Riddler, The Penguin and Catwoman, both in Gotham City… and in space.\",\"release_date\":\"2016-10-08\",\"genre_ids\":[28,16,35],\"id\":411736,\"original_title\":\"Batman: Return of the Caped Crusaders\",\"original_language\":\"en\",\"title\":\"Batman: Return of the Caped Crusaders\",\"backdrop_path\":\"\\/4HriXH0XmceHwW6FVFcl3KPUaVY.jpg\",\"popularity\":3.806869,\"vote_count\":1,\"video\":false,\"vote_average\":8},{\"poster_path\":\"\\/hCyfYMOkKg6AKXprg264Awcn64J.jpg\",\"adult\":false,\"overview\":\"Biopic of the great French ocean-going adventurer and filmmaker\",\"release_date\":\"2016-10-12\",\"genre_ids\":[12,36],\"id\":332794,\"original_title\":\"L'Odyssée\",\"original_language\":\"fr\",\"title\":\"The Odyssey\",\"backdrop_path\":\"\\/gUfTAuMD1DwGVCaVqBD18Rnq7UL.jpg\",\"popularity\":3.552855,\"vote_count\":3,\"video\":false,\"vote_average\":6},{\"poster_path\":\"\\/39jyxfevRRuLGbh88F5eIzRtOeo.jpg\",\"adult\":false,\"overview\":\"Husband and wife living in a dystopic future become trapped in a mysterious time loop — one that may have something to do with an ongoing battle between an omnipotent corporation and a ragtag band of rebels.\",\"release_date\":\"2016-09-16\",\"genre_ids\":[53,878],\"id\":410199,\"original_title\":\"ARQ\",\"original_language\":\"en\",\"title\":\"ARQ\",\"backdrop_path\":\"\\/q8kc6CzBfmIAHplBOVUWiAYrA56.jpg\",\"popularity\":3.224411,\"vote_count\":80,\"video\":false,\"vote_average\":5.99},{\"poster_path\":\"\\/pfhYMnzacbE4dqeQxGvhNk74Atv.jpg\",\"adult\":false,\"overview\":\"A young man is on a quest to find a mythical strain of cannabis called the SUPERPSYCHOCEBU. Along the way, he meets characters that appear to have lost their minds after trying it, one of them even possessed by the Devil, at which point he starts questioning whether he’s in for some good weed or just one hell of a bad trip.\",\"release_date\":\"2016-10-15\",\"genre_ids\":[35],\"id\":421043,\"original_title\":\"Superpsychocebu\",\"original_language\":\"en\",\"title\":\"Superpsychocebu\",\"backdrop_path\":null,\"popularity\":3.1856,\"vote_count\":0,\"video\":false,\"vote_average\":0},{\"poster_path\":\"\\/jhNw2WMsutZi1RsNCAKd86Ae0x5.jpg\",\"adult\":false,\"overview\":\"For Justin Timberlake it's the final two dates of his 20\\/20 Experience World Tour at the MGM Grand Garden Arena in Las Vegas. Surrounded by the 25 band members of The Tennessee Kids and featuring show-stopping performances from one of the highest-grossing tours of the decade, the film is a culmination of the singer’s 134 shows and 2 years on the road.\",\"release_date\":\"2016-10-12\",\"genre_ids\":[10402,99],\"id\":408619,\"original_title\":\"Justin Timberlake + The Tennessee Kids\",\"original_language\":\"en\",\"title\":\"Justin Timberlake + The Tennessee Kids\",\"backdrop_path\":\"\\/mraeO6dKRPv8dBKTUamqUz1OdGr.jpg\",\"popularity\":3.151274,\"vote_count\":1,\"video\":false,\"vote_average\":8}],\"dates\":{\"maximum\":\"2016-10-21\",\"minimum\":\"2016-09-09\"},\"total_pages\":56,\"total_results\":1106}");
//
//        JSONArray movieJsonResults = null;
//
//        try {
//
//            movieJsonResults = response.getJSONArray("results");
//            movies.addAll(Movie.fromJSONArray(movieJsonResults));
//            movieAdapter.notifyDataSetChanged();
//
//            //signal refresh has finished:
//            swipeContainer.setRefreshing(false);
//
//            Log.d("DEBUG", movies.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//    }



}
