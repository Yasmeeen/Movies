package com.example.yasminabdelhay.movies2;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;

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
import java.util.Collections;


public class
        MovieData extends android.support.v4.app.Fragment {

    private final String LOG_TAG = "hi";
    GridView gridView;
    ImageAdapter imageAdapter;
    View rootView;
    View rootView2;
    String MovieJsonStr = null;
    String pathInCertainPositio=null;
    String movie_path = null;
    ArrayList<String> poster_pathes1 =new ArrayList<>();
    ArrayList<String> poster_id =new ArrayList<>();
    ArrayList<String> mposter_pathesList =new ArrayList<>();
   // NameListnere nameListnere =null;
    NameListnere nameListneres=null;
    SharedPreferences  SharedPref;
    String sortType ;



    Movie movie=new Movie(MovieJsonStr);


    public MovieData() {
        setHasOptionsMenu(true);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        // read ListPreference option
          SharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
         sortType= SharedPref.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_popular));

        //read favorite poster path from shared prefrence

        String moviePathString;
        moviePathString=SharedPref.getString("poster_path", "");

        String IDString;
        IDString=SharedPref.getString("movie_id", "");
         Log.v("bigstring",IDString);
        int sharedPreferencesStringNumberofFavorit;
        sharedPreferencesStringNumberofFavorit=SharedPref.getInt("favorit_movie_number", 0);

        String[] parts = moviePathString.split("-", sharedPreferencesStringNumberofFavorit);
        String[] parts2 = IDString.split(",", sharedPreferencesStringNumberofFavorit);
        Log.v("number", String.valueOf(sharedPreferencesStringNumberofFavorit));
        poster_pathes1.clear();
      //  Log.v("movie_path", movie_path);
      for (int i=0;i<sharedPreferencesStringNumberofFavorit;i++){
            poster_pathes1.add(i, parts[i]);
           poster_id.add(i, parts2[i]);

        }
//        Log.v("number id", (poster_id.get(1)));



        Collections.reverse(poster_pathes1);

        if(sortType.equals(getString(R.string.pref_units_popular))){
            new MovieTask().execute("popular");
        }
        else if (sortType.equals(getString(R.string.pref_units_most_likely)))
        {
            new MovieTask().execute("Most Likely");

        }

        else {


            gridView.setAdapter(null);
            imageAdapter = new ImageAdapter(getActivity(), poster_pathes1);
            gridView.setAdapter(null);
            gridView.setAdapter(imageAdapter);
            imageAdapter.notifyDataSetChanged();





        }


    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {



        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //rootView2 = inflater.inflate(R.layout.fragment_detail, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gv_posters);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @TargetApi(Build.VERSION_CODES.HONEYCOMB)

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                if (sortType.equals(getString(R.string.pref_units_most_likely)) || sortType.equals(getString(R.string.pref_units_popular))) {
                    int movie_id = 0;

                      try {
                        movie_id = new Movie(MovieJsonStr).getIDMovieFromJson().get(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String movie_name = null;

                    try {
                        movie_name = new Movie(MovieJsonStr).getoriginaltitleataFromJson().get(position);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    try {
                        movie_path = new Movie(MovieJsonStr).getPosterPathDataFromJson().get(position);
                        pathInCertainPositio = movie_path;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.favorite);


                    nameListneres.selsctedName(movie_name, MovieJsonStr, position, movie_path, 0);



                } else {

                    String movie_name=null;
                    String movie_id = poster_id.get(position);

                    Log.v("id movie", movie_id);


                    nameListneres.selsctedName(movie_name, MovieJsonStr, position, movie_path, Integer.parseInt(movie_id));
                }

            }
        });


        return rootView;

    }

    public class MovieTask extends AsyncTask<String, Void, Movie> {
        @Override
        protected Movie doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;




            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/";
                final String MOVIE_POPULAR_BASE_URL = "3/movie/popular?";
                final String MOVIE_MOST_LIKLY_BASE_URL = "3/movie/top_rated?";
                final String api_key = "api_key";



                Uri buildUri;
                if (params[0].equals("popular")) {
                    buildUri = Uri.parse(MOVIE_BASE_URL + MOVIE_POPULAR_BASE_URL).buildUpon()
                            .appendQueryParameter(api_key, BuildConfig.OPEN_MOVIE_AAP_API_KEY)
                            .build();
                } else {
                    buildUri = Uri.parse(MOVIE_BASE_URL + MOVIE_MOST_LIKLY_BASE_URL).buildUpon()
                            .appendQueryParameter(api_key, BuildConfig.OPEN_MOVIE_AAP_API_KEY)
                            .build();
                }
//http://api.themoviedb.org/3/movie/popular?api_key=67c3e9a8357661792e5956106c3de3f6
                URL url = new URL(buildUri.toString());
             //   Log.v(LOG_TAG, "Built URI " + buildUri.toString());

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

                MovieJsonStr = buffer.toString();
            //    Log.v(LOG_TAG, "MOVIE Json String  " + MovieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Movie Json String Not found", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

                return new Movie(MovieJsonStr);

        }


        @Override
        protected void onPostExecute(Movie movie) {

            try {
                if (movie.getPosterPathDataFromJson() != null) {

                    imageAdapter = new ImageAdapter(getActivity(), movie.getPosterPathDataFromJson());
                    gridView.setAdapter(imageAdapter);
                } else {
                    Log.v(LOG_TAG, "error ");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

public void setNameListneres(NameListnere nameListneres){
   this.nameListneres=nameListneres;


}

}





