package com.codepath.flicks.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;

/**
 * Created by Alicia P on 17-Oct-16.
 */

public class Utils {

    private final static String movieDBAPIkey = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private final static String youTubeAPIkey = "AIzaSyAT3LmVNkOC6Cnf_z7ch6GR3ZEeiPDN_Iw";

    private final static String baseURL = "https://api.themoviedb.org/3/movie/";

    private final static AsyncHttpClient client = new AsyncHttpClient();


    public static String getMovieDBAPIkey() {
        return movieDBAPIkey;
    }

    public static String getYouTubeAPIkey() {
        return youTubeAPIkey;
    }

    public static AsyncHttpClient getClient() {
        return client;
    }

    public static String getBaseURL() {
        return baseURL;
    }


    public static boolean checkForConnectivity(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
