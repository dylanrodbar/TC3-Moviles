package com.example.dylanrodbar.reproductor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dylanrodbar on 13/3/2018.
 */

public class AdapterListView extends ArrayAdapter<Song> {

    private final Activity context;
    private ArrayList<Song> songs = null;

    public AdapterListView(Activity context, ArrayList<Song> songs) {
        super(context, R.layout.custom_row_view, songs);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.songs = songs;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.custom_row_view, null,true);

        TextView txtArtist = (TextView) rowView.findViewById(R.id.artist);
        TextView txtSong = (TextView) rowView.findViewById(R.id.song);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.songImage);

        txtArtist.setText(songs.get(position).getArtistName());
        txtSong.setText(songs.get(position).getSongName());

        Bitmap bm = BitmapFactory.decodeFile(songs.get(0).getPath());
        imageView.setImageBitmap(bm);

        //imageView.setImageResource(imgid[position]);
        return rowView;

    };



}
