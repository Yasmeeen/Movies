package com.example.yasminabdelhay.movies2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sara on 25-Mar-16.
 */
public class ImageAdapter extends BaseAdapter{
   // List<String> poster_paths;
    ArrayList<String> original_title = new ArrayList<String>();
    ArrayList<String> poster_paths = new ArrayList<String>();
    String poster_path;

    Context context;
    public ImageAdapter(Context context , ArrayList poster_paths  )
    {
        this.context=context;
        this.poster_paths=poster_paths;
    }

    public ImageAdapter( String poster_path  )
    {
        this.poster_path=poster_path;
    }
    @Override
    public int getCount() {
        return poster_paths.size();
    }

    @Override
    public String getItem(int i) {
        return poster_paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.grid_item,null);
            holder.imageView=(ImageView)convertView.findViewById(R.id.iv_item);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        Picasso.with(context).load(getItem(i)).into(holder.imageView);
        return convertView;
    }
    class ViewHolder{
        ImageView imageView;
    }


}


