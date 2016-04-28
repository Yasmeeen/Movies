package com.example.yasminabdelhay.movies2;


import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Movie  {
    String MovieJsonStr;
    String MovieJsonStr2;
    GridView gridView;
    ImageAdapter imageAdapter;
    View rootView;


    public Movie(String MovieJsonStr) {
        this.MovieJsonStr = MovieJsonStr;
    }
    public Movie(){


    }


    public ArrayList<String> getPosterPathDataFromJson()
            throws JSONException {

        final String Movie_result = "results";
        final String Movie_poster = "poster_path";

        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONArray result_Array = BigJsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;
        //  JSONObject TitleJson;

        ArrayList<String> Poster_PathsList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            BlockIntoArray = BigJsonObject.getJSONArray(Movie_result).getJSONObject(i);
            Poster_PathsList.add("http://image.tmdb.org/t/p/w185" + (String) BlockIntoArray.get(Movie_poster));
        }


        return Poster_PathsList;

    }

    public ArrayList<String> getoverviewDataFromJson()
            throws JSONException {

        final String Movie_result = "results";
        final String overview = "overview";

        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONArray result_Array = BigJsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;
        //  JSONObject TitleJson;

        ArrayList<String> overviewList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            BlockIntoArray = result_Array.getJSONObject(i);
            overviewList.add((String) BlockIntoArray.get(overview));  //the name it self we should get
        }


        return overviewList;

    }

    public ArrayList<String> getoriginaltitleataFromJson()
            throws JSONException {

        final String Movie_result = "results";
        final String original_title = "original_title";

        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONArray result_Array = BigJsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;

        ArrayList<String> original_title_List = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            BlockIntoArray = result_Array.getJSONObject(i);
            original_title_List.add((String) BlockIntoArray.get(original_title));
        }


        return original_title_List;

    }

    public ArrayList<String> getReleaseDateFromJson()
            throws JSONException {

        final String Movie_result = "results";
        final String release_date = "release_date";

        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONArray result_Array = BigJsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;

        ArrayList<String> release_date_List = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            BlockIntoArray = result_Array.getJSONObject(i);
            release_date_List.add((String) BlockIntoArray.get(release_date));
        }


        return release_date_List;

    }


    public ArrayList<Double> getVoteAverageFromJson()
            throws JSONException {

        final String Movie_result = "results";
        String vote_average = "vote_average";

        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONArray result_Array = BigJsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;

        ArrayList<Double> vote_average_List = new ArrayList<>();

        for (int i = 0; i < 20; i++) {

            BlockIntoArray = result_Array.getJSONObject(i);
            vote_average_List.add((double) BlockIntoArray.getDouble(vote_average));

        }

            return vote_average_List;


    }

    public ArrayList<Integer> getIDMovieFromJson()
            throws JSONException {

        final String Movie_result = "results";
        final String Movie_id = "id";

        JSONObject BigJsonObject = new JSONObject(MovieJsonStr);
        JSONArray result_Array = BigJsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;
        //  JSONObject TitleJson;

        ArrayList<Integer> movie_id_List = new ArrayList<>();
        for (int i = 0; i < 20; i++) {

            BlockIntoArray = BigJsonObject.getJSONArray(Movie_result).getJSONObject(i);
            movie_id_List.add((Integer) BlockIntoArray.get(Movie_id));
           Log.i("ID", String.valueOf(movie_id_List.get(i)));
        }


        return movie_id_List;


    }
}
