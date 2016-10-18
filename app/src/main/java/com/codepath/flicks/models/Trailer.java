package com.codepath.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Alicia P on 17-Oct-16.
 */

public class Trailer {

    private String trailerID;
    private String trailerType;

    //constructors
    public Trailer(){
        super();
    }

    public Trailer(JSONObject jsonObject) throws JSONException{
        trailerID   = jsonObject.getString("key");
        trailerType = jsonObject.getString("type");
    }

    public String getTrailerID() {
        return trailerID;
    }

    public String getTrailerType() {
        return trailerType;
    }

    //convert Json object into ArrayList
    public static ArrayList<Trailer> fromJSONArray(JSONArray array){
        ArrayList<Trailer> results = new ArrayList<>();

        for(int i=0; i<array.length(); i++){
            try {
                results.add(new Trailer(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
