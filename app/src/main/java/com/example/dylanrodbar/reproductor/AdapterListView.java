package com.example.dylanrodbar.reproductor;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dylanrodbar on 13/3/2018.
 */

public class AdapterListView extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] artistName;
    private final String[] songName;
    private final Integer[] imgid;

    public AdapterListView(Activity context, String[] artistName, String[] songName, Integer[] imgid) {
        super(context, R.layout.custom_row_view, artistName);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.artistName=artistName;
        this.songName = songName;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_row_view, null,true);

        TextView txtArtist = (TextView) rowView.findViewById(R.id.artist);
        TextView txtSong = (TextView) rowView.findViewById(R.id.song);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.songImage);

        txtArtist.setText(artistName[position]);
        txtSong.setText(songName[position]);
        imageView.setImageResource(imgid[position]);
        return rowView;

    };



}
