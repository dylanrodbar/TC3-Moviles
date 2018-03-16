package com.example.dylanrodbar.reproductor;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.dylanrodbar.cointransformer.MESSAGE";
    ArrayList<Song> songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list;
        songs = new ArrayList<>();

        getMusic();
        AdapterListView adapter=new AdapterListView(this, songs);
        list=(ListView)findViewById(R.id.listSongs);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //String Slecteditem= itemname[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                openMusicDetail(position);

            }
        });


    }

    public void openMusicDetail(int position) {
        String songName = songs.get(position).getSongName();
        String artistName = songs.get(position).getArtistName();
        String albumName = songs.get(position).getAlbumName();
        String path = songs.get(position).getPath();
        String data = songs.get(position).getData();
        long aId = songs.get(position).getAlbumId();
        long sId = songs.get(position).getSongId();
        Intent intent = new Intent(this, MusicDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        intent.putExtra("song", songName);
        intent.putExtra("artist", artistName);
        intent.putExtra("album", albumName);
        intent.putExtra("path", path);
        intent.putExtra("data", data);
        intent.putExtra("albumid", aId);
        intent.putExtra("songid", sId);
        intent.putExtra("songs", songs);
        startActivity(intent);
    }


    public void getMusic() {

        int ultimoD = 0;
        TextView t = findViewById(R.id.txtSongDetail);
        TextView t1 = findViewById(R.id.txtArtistDetail);
        TextView t2 = findViewById(R.id.txtAlbumDetail);
        ImageView circle = findViewById(R.id.imageView2);
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,    // filepath of the audio file
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID// context id/ uri id of the file
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";


        Cursor songCursor = contentResolver.query(songUri, projection, selection, null, sortOrder);
        if (songCursor != null && songCursor.moveToFirst()) {

            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int data = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int sId = songCursor.getColumnIndex(projection[5]);
            int aId = songCursor.getColumnIndex(projection[6]);


            do {
                Song song = new Song();
                String songTitleS = songCursor.getString(songTitle);
                String artistTitleS = songCursor.getString(artistTitle);
                String albumTitleS = songCursor.getString(albumTitle);
                String dataS = songCursor.getString(data);

                song.setSongName(songTitleS);
                song.setArtistName(artistTitleS);
                song.setAlbumName(albumTitleS);
                song.setData(dataS);
                song.setAlbumId(aId);
                song.setSongId(sId);
                songs.add(song);
            } while (songCursor.moveToNext());

        }




        ContentResolver musicResolve = getContentResolver();
        Uri smusicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor music = musicResolve.query(smusicUri, null         //should use where clause(_ID==albumid)
                , null, null, null);

        int cont = 0;
        if (music != null && music.moveToFirst()) {

            long x = music.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

            do {

                //String thisArt = music.getString(x);

                songs.get(cont).setAlbumId(x);

                cont++;
            } while (music.moveToNext());
        }

    }

    public void myMusicOptionClicked(View view) {
        Intent intent = new Intent(this, MusicListActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        startActivity(intent);
    }

    public void topMusicOptionClicked(View view) {

    }

    public void recentMusicOptionClicked(View view) {

    }

    public void recommendedMusicOptionClicked(View view) {

    }

    public void currentSongClicked(View view) {
        Intent intent = new Intent(this, MusicDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        startActivity(intent);
    }
}
