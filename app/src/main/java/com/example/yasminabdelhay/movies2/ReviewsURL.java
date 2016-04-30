package com.example.yasminabdelhay.movies2;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by yasmin abdelhay on 4/21/2016.
 */
public class ReviewsURL extends AsyncTask<Integer, Void, ArrayList<String>> {
    String MovieJsonStr2;
    View rootView2;
    private ArrayAdapter<String> adapter;
    ListView listView;
    Context context;
    LayoutInflater inflater;
    ViewGroup container;

    ArrayList<String> Tralier_url = new ArrayList<>();
    ReviewsURL(Context context, View rootView2){
        this.context=context;
        this.rootView2=rootView2;

    }





    @Override
    protected ArrayList<String> doInBackground(Integer... movie_id) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {

            final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
            final String api_key = "api_key";

            Uri buildUri;
            buildUri = Uri.parse(MOVIE_BASE_URL + movie_id[0] + "/reviews?").buildUpon()
                    .appendQueryParameter(api_key, BuildConfig.OPEN_MOVIE_AAP_API_KEY)
                    .build();

            URL url = new URL(buildUri.toString());
            Log.v("y", "Built URI " + buildUri.toString());
            Log.i("y", "Built URI " + buildUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");


            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {

                return null;
            }

            MovieJsonStr2 = buffer.toString();
            Log.v("DDDDDDDDDDDDDDDD", "MOVIE Json String  " + MovieJsonStr2);

        } catch (IOException e) {
            Log.e("DDDDDDDDDDDDDDDD", "Movie Json String Not found", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("HI", "Error ", e);
                }
            }
        }

        Log.i("hi", MovieJsonStr2);
        try {
            Log.i("Hi", String.valueOf(getothernameDataFromJson()));
            return getothernameDataFromJson();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }



    public ArrayList<String> getothernameDataFromJson()
            throws JSONException {

        final String Movie_result = "results";
        final String author = "author";
        final String content = "content";
        String movie_Tralier_Url;

        JSONObject JsonObject = new JSONObject(MovieJsonStr2);
        JSONArray result_Array = JsonObject.getJSONArray(Movie_result);
        JSONObject BlockIntoArray;

        ArrayList<String> movie_Reviews_Data = new ArrayList<>();
        for (int i = 0; i < result_Array.length(); i++) {

            BlockIntoArray = result_Array.getJSONObject(i);
            movie_Reviews_Data.add(i, ( (String) BlockIntoArray.get(author))+"::"+( (String) BlockIntoArray.get(content)));
            Log.v("y", "Built Reviews " + movie_Reviews_Data.get(i));
        }


        return movie_Reviews_Data;


    }
    @Override
    protected void onPostExecute(ArrayList<String> Reviews) {
        ArrayList<String> list_item_Reviews = new ArrayList<String>();
        String data = null;


        adapter =new ArrayAdapter<String>(context, R.layout.reviews_item_list, R.id.Reviews_item, Reviews);
        if(null==listView )
            Log.e("211","list Viewis null ");
        final ListView listView2= (ListView) rootView2.findViewById(R.id.movie_reviews_list);
        setListViewHeightBasedOnChildren(listView2);
        listView2.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = ListView.MeasureSpec.makeMeasureSpec(listView.getWidth(), ListView.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, ListView.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    }

}

