package com.example.dylanrodbar.reproductor;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicDetailActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;
    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        boolean pause = false;
        String song = getIntent().getStringExtra("song");
        String artist = getIntent().getStringExtra("artist");
        String album = getIntent().getStringExtra("album");
        String path = getIntent().getStringExtra("path");
        String data = getIntent().getStringExtra("data");
        long aID = getIntent().getLongExtra("albumid", 0);
        long sID = getIntent().getLongExtra("songid", 0);
        TextView tSong = findViewById(R.id.txtSongDetail);
        TextView tArtist = findViewById(R.id.txtArtistDetail);
        TextView tAlbum = findViewById(R.id.txtAlbumDetail);
        ImageView image = findViewById(R.id.imageView2);

        tSong.setText(song);
        tArtist.setText(artist);
        tAlbum.setText(album);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(data);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        SeekBar volumeSeekBar = findViewById(R.id.volumeSeekBar);
        volumeSeekBar.setMax(maxVolume);
        volumeSeekBar.setProgress(currentVolume);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar advancedSeekBar = findViewById(R.id.advanceSeekBar);
        int duration = mediaPlayer.getDuration();
        int progress = mediaPlayer.getCurrentPosition();
        advancedSeekBar.setMax(duration);
        advancedSeekBar.setProgress(progress);

        advancedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        //Bitmap bm = BitmapFactory.decodeFile(path);
        //image.setImageBitmap(bm);

        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        String.valueOf(sID), Toast.LENGTH_SHORT);

        toast1.show();


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

                            drawSongRotation();
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




    public void drawSongRotation() {
        ImageView relativeSong = findViewById(R.id.imageView2);
        ImageButton imgV = findViewById(R.id.playSongButton);
        float deg = relativeSong.getRotation() + 70F;
        relativeSong.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
    }


    public void buttonPlayPauseClicked(View view) {
        ImageButton img = findViewById(R.id.playSongButton);
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            img.setImageResource(R.drawable.botonpausar);
        }
        else {
            mediaPlayer.start();
            img.setImageResource(R.drawable.botonreproducir);
        }
    }



}
