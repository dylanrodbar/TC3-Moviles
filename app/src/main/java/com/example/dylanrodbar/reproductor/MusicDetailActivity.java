package com.example.dylanrodbar.reproductor;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicDetailActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);

        String song = getIntent().getStringExtra("song");
        String artist = getIntent().getStringExtra("artist");
        String album = getIntent().getStringExtra("album");
        String path = getIntent().getStringExtra("path");
        String data = getIntent().getStringExtra("data");
        TextView tSong = findViewById(R.id.txtSongDetail);
        TextView tArtist = findViewById(R.id.txtArtistDetail);
        TextView tAlbum = findViewById(R.id.txtAlbumDetail);
        ImageView image = findViewById(R.id.imageView2);

        tSong.setText(song);
        tArtist.setText(artist);
        tAlbum.setText(album);

        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(data);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bitmap bm = BitmapFactory.decodeFile(path);
        image.setImageBitmap(bm);

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Thread tr = Thread.currentThread();

                while (true) {



                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dibujarRotacionCancion();
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });


                }

            }
        };

        Thread t = new Thread(r, "T1");
        t.start();


        if(ContextCompat.checkSelfPermission(MusicDetailActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(MusicDetailActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(MusicDetailActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
            else {

                ActivityCompat.requestPermissions(MusicDetailActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        }
        else {

        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch(requestCode) {
            case MY_PERMISSION_REQUEST:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                    if(ContextCompat.checkSelfPermission(MusicDetailActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
        }
    }




    public void dibujarRotacionCancion() {
        ImageView relativeSong = findViewById(R.id.imageView2);
        ImageButton imgV = findViewById(R.id.playSongButton);
        float deg = relativeSong.getRotation() + 70F;
        relativeSong.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());



    }



}
