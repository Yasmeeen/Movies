package com.example.yasminabdelhay.movies2;


import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements NameListnere {

boolean mtwopanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout flpanel2 = (FrameLayout) findViewById(R.id.flpanel_two);
        if(null==flpanel2){
            mtwopanel=false;
        }
        else {
            mtwopanel=true;
            if(savedInstanceState ==null){

            }
        }

             MovieData movieData=new MovieData();
             movieData.setNameListneres(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void selsctedName(String movie_name,String MovieJsonStr,int position,String  movie_path,int movie_id ) {
            if(mtwopanel){
                DetailFragment detailFragment=new DetailFragment();
                Bundle extras =new Bundle();
                extras.putString("movie_name",movie_name);
                extras.putString("MovieJsonStr",MovieJsonStr);
                extras.putInt("position", position);
                extras.putString("movie_path", movie_path);
                extras.putInt("movie_id", movie_id);
                detailFragment.setArguments(extras);

                getSupportFragmentManager().beginTransaction().replace(R.id.flpanel_two,detailFragment).commit();

            }
        else {
                Intent intent = new Intent(this, DetailActivity.class)
                        .putExtra("pos", position).putExtra("str", MovieJsonStr)
                        .putExtra("movie_path", movie_path).putExtra("movie_id", movie_id).putExtra("movie_name", movie_name);
                startActivity(intent);
            }
    }
}