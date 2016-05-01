package com.example.yasminabdelhay.movies2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

public  class DetailFragment extends Fragment {
    String MovieJsonStr;
    int position;
    String movie_path=null ;
    int movie_id;
    String part1=null;
    double movie_vote = 0;

    SharedPreferences  SharedPref;
    String sortType ;
    String movie_overview = null;
    String movie_date = null;
    String movie_title = null;

    public DetailFragment() {
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        MovieJsonStr =getArguments().getString("MovieJsonStr");
        position = (int) getArguments().get("position");
        movie_path = (String) getArguments().get("movie_path");
        movie_id = (int) getArguments().get("movie_id");
        movie_title= (String) getArguments().get("movie_name");
        movie_date = (String) getArguments().get("movie_date");
        movie_overview = (String) getArguments().get("movie_overview");
        movie_vote= (double) getArguments().get("movie_rate");



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);
        int id = 0;

        String poster_pathes = null;
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        SharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortType= SharedPref.getString(getString(R.string.pref_units_key), getString(R.string.pref_units_popular));
        final ImageButton imageButton =(ImageButton) rootView.findViewById(R.id.favorite);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("str")) {
            MovieJsonStr = intent.getStringExtra("str");
        }
        if (intent != null && intent.hasExtra("pos")) {
            position = intent.getIntExtra("pos", -1);
        }

        if (intent != null && intent.hasExtra("movie_id")) {
            movie_id = intent.getIntExtra("movie_id", -1);
        }

        Log.v("Fragment_id", movie_id + "");
        if (intent != null && intent.hasExtra("movie_path")) {
            movie_path = intent.getStringExtra("movie_path");
        }

        if (intent != null && intent.hasExtra("movie_name")) {
            movie_title = intent.getStringExtra("movie_name");
        }

        if (intent != null && intent.hasExtra("movie_date")) {
            movie_date = intent.getStringExtra("movie_date");
        }

        if (intent != null && intent.hasExtra("movie_rate")) {
            movie_vote = intent.getDoubleExtra("movie_rate", 0.0);
        }

        if (intent != null && intent.hasExtra("movie_overview")) {
            movie_overview = intent.getStringExtra("movie_overview");
        }



        if(sortType.equals(getString(R.string.pref_units_most_likely)) || sortType.equals(getString(R.string.pref_units_popular))) {




            try {
                new MovieURL(getContext(), rootView).execute(new Movie(MovieJsonStr).getIDMovieFromJson().get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                new ReviewsURL(getContext(), rootView).execute(new Movie(MovieJsonStr).getIDMovieFromJson().get(position));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {

                movie_title = new Movie(MovieJsonStr).getoriginaltitleataFromJson().get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((TextView) rootView.findViewById(R.id.movie_title))
                    .setText(movie_title);


            final ImageView imageView = ((ImageView) rootView.findViewById(R.id.movie_image));

            try {
                poster_pathes = new Movie(MovieJsonStr).getPosterPathDataFromJson().get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Picasso.with(getActivity()).load(poster_pathes).into(imageView);



            //  String date=null;
            try {
                movie_date = new Movie(MovieJsonStr).getReleaseDateFromJson().get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // date= movie_date.charAt(0)+movie_date.charAt(1)+movie_date.charAt(2)+movie_date.charAt(3)+"";

            String[] parts = movie_date.split("-", 2);
             part1 = parts[0];
            String part2 = parts[1];

            ((TextView) rootView.findViewById(R.id.movie_date))
                    .setText(part1);




            try {
                movie_vote = new Movie(MovieJsonStr).getVoteAverageFromJson().get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ((TextView) rootView.findViewById(R.id.movie_vote))
                    .setText((movie_vote) + "/10");


            try {
                movie_overview = new Movie(MovieJsonStr).getoverviewDataFromJson().get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ((TextView) rootView.findViewById(R.id.movie_overview))
                    .setText(movie_overview);
            // ImageView imageView1= (ImageView) rootView.findViewById(R.id.w_star_image);



            try {
                id = new Movie(MovieJsonStr).getIDMovieFromJson().get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        else{


               imageButton.setImageResource(R.drawable.star2);

                new MovieURL(getContext(), rootView).execute(movie_id);


                new ReviewsURL(getContext(), rootView).execute(movie_id);

              ((TextView) rootView.findViewById(R.id.movie_title))
                    .setText(movie_title);


            final ImageView imageView = ((ImageView) rootView.findViewById(R.id.movie_image));

            Picasso.with(getActivity()).load(movie_path).into(imageView);


            ((TextView) rootView.findViewById(R.id.movie_date))
                    .setText(movie_date);

            ((TextView) rootView.findViewById(R.id.movie_vote))
                    .setText((movie_vote) + "/10");


            ((TextView) rootView.findViewById(R.id.movie_overview))
                    .setText(movie_overview);
        }

        final String finalMovie_path = movie_path;
        final String finalid = id +",";
        final String finalMovieTitle=movie_title;
        final String finalMovieJason=MovieJsonStr;
        final String finaldate=part1;
        final String finaloverViewn=movie_overview;
        final String finalrate=movie_vote+"";

        Log.v("idString", finalid);


        imageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                imageButton.setImageResource(R.drawable.star2);
                SharedPreferences
                        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPref.edit();

                String movie_paths = sharedPref.getString("poster_path", "");


                String jason = sharedPref.getString("jason_string", "");
                String date = sharedPref.getString("date_string", "");
                String overView = sharedPref.getString("overViww_string", "");
                String rate = sharedPref.getString("rate_string", "");

                String movie_title = sharedPref.getString("movie_title", "");
                String movie_id = sharedPref.getString("movie_id", "");

                int favorit_movie_number;
                favorit_movie_number = sharedPref.getInt("favorit_movie_number", 0);

                movie_paths += finalMovie_path;
                movie_id += finalid;
                movie_title +=finalMovieTitle;
                jason +=finalMovieJason;
                date +=finaldate;
                overView +=finaloverViewn;
                rate +=finalrate;
                editor.putString("poster_path",  movie_paths+ "-");
                ++favorit_movie_number;
                editor.putInt("favorit_movie_number", favorit_movie_number);
                editor.putString("movie_id", movie_id);
                editor.putString("movie_title", movie_title + "&");
                editor.putString("date_string", date + "-");
                editor.putString("overViww_string", overView + "&");
                editor.putString("rate_string", rate + "-");
                editor.putString("jason_string", jason + "$");



                editor.commit();
                editor.apply();

            }

        });





        return rootView;
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

    public class MovieURL extends AsyncTask<Integer, Void, ArrayList<String>> {
        String MovieJsonStr2;
        ListView listView;
        View rootView;
        ArrayList<String> Tralier_url = new ArrayList<>();

        private ArrayAdapter<String> adapter;
        Context context;
        //LayoutInflater inflater;
        ViewGroup container;

        MovieURL(Context context, View rootView) {
            this.context = context;
            //this.inflater=inflater;
            //  this.listView=listView;
            this.rootView = rootView;

        }


        @Override
        protected ArrayList<String> doInBackground(Integer... movie_id) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String api_key = "api_key";

                Uri buildUri;
                buildUri = Uri.parse(MOVIE_BASE_URL + movie_id[0] + "/videos?").buildUpon()
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
                return getTralierDataFromJson();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        public ArrayList<String> getTralierDataFromJson()
                throws JSONException {

            final String Movie_result = "results";
            final String key = "key";
            String movie_Tralier_Url;

            JSONObject JsonObject = new JSONObject(MovieJsonStr2);
            JSONArray result_Array = JsonObject.getJSONArray(Movie_result);
            JSONObject BlockIntoArray;

            ArrayList<String> movie_Tralier_Data = new ArrayList<>();
            for (int i = 0; i < result_Array.length(); i++) {

                BlockIntoArray = result_Array.getJSONObject(i);
                movie_Tralier_Data.add(i, ("https://www.youtube.com/watch?v=" + (String) BlockIntoArray.get(key)));
                Log.v("y", "Built URI Tralier " + movie_Tralier_Data.get(i));
            }


            return movie_Tralier_Data;


        }


        @Override
        protected void onPostExecute(final ArrayList<String> Tralier_url) {
            ArrayList<String> list_item_tralier = new ArrayList<String>();
            String data = null;

            //  ListView listView = new  ListView(context);
            Log.v("tralername", String.valueOf(Tralier_url.size()));
            //  ListView myListView2= (ListView) rootView.findViewById(R.id.Tralier_item);
            ArrayList<String> Traliers = new ArrayList<String>();
            for (int i = 0; i < Tralier_url.size(); i++) {
                list_item_tralier.add("Tralier" + (i + 1));
                Traliers.add(list_item_tralier.get(i));
                Log.v("traler name", Traliers.get(i));
            }


            for (int i = 0; i < Traliers.size(); i++) {
                Log.v("Traler" + i, Traliers.get(i));
            }
            adapter = new ArrayAdapter<String>(context, R.layout.tralier_item_list, R.id.Tralier_item, Traliers);
            Log.v("Adapter", String.valueOf(adapter));

            Intent intent = new Intent(Intent.ACTION_VIEW);

            final ListView listView2 = (ListView) rootView.findViewById(R.id.movie_tralier_list);
            listView2.setAdapter(adapter);
            setListViewHeightBasedOnChildren(listView2);
            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Tralier_url.get(position).toString()));
                    startActivity(intent);
                }
            });


        }
    }
}