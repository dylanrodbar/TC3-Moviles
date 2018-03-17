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
import android.provider.ContactsContract;
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
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicDetailActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    ArrayList<Song> songs;
    ArrayList<Song> auxiliarSongs;
    ArrayList<Song> aleatorySongs;
    ArrayList<Song> queue;
    private boolean repeat = false;
    private boolean aleatory = false;
    SeekBar advancedSeekBar;
    int countSongs = 0;
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
        songs = new ArrayList<Song>();
        auxiliarSongs = new ArrayList<Song>();
        aleatorySongs = new ArrayList<Song>();
        queue = new ArrayList<Song>();
        songs = (ArrayList<Song>) getIntent().getSerializableExtra("songs");
        long aID = getIntent().getLongExtra("albumid", 0);
        long sID = getIntent().getLongExtra("songid", 0);
        TextView tSong = findViewById(R.id.txtSongDetail);
        TextView tArtist = findViewById(R.id.txtArtistDetail);
        TextView tAlbum = findViewById(R.id.txtAlbumDetail);
        ImageView image = findViewById(R.id.imageView2);

        /*Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        String.valueOf(aID), Toast.LENGTH_SHORT);

        toast1.show();*/

        tSong.setText(song);
        tArtist.setText(artist);
        tAlbum.setText(album);

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(data);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    nextSong();
                }
            });
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
        advancedSeekBar = findViewById(R.id.advanceSeekBar);

        configureSeekBar();







        createAuxiliarArray();
        createQueue();
        createAleatory();
        createThread();
        createTimer();




        Bitmap bm = BitmapFactory.decodeFile(path);

        //Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        //Uri path1 = ContentUris.withAppendedId(artworkUri, aID);
        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/artpop");
        Glide.with(getApplicationContext()).load(bm).apply(new RequestOptions().circleCrop()).into(image);
        //Glide.with(image.getContext()).load(path1).into(image);
        //image.setImageBitmap(bm);









    }

    public void configureSeekBar() {
        int duration = mediaPlayer.getDuration();
        int progress = mediaPlayer.getCurrentPosition();
        advancedSeekBar.setMax(duration);
        advancedSeekBar.setProgress(progress);

        advancedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) mediaPlayer.seekTo(progress); //OMG
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void createAuxiliarArray() {
        for(Song s: songs) {
            Song so = new Song();
            so.setSongName(s.getSongName());
            so.setArtistName(s.getArtistName());
            so.setAlbumName(s.getAlbumName());
            so.setAlbumId(s.getAlbumId());
            so.setSongId(s.getSongId());
            so.setData(s.getData());
            auxiliarSongs.add(so);
        }
    }

    public void createQueue() {
        for(Song s: songs) {
            Song so = new Song();
            so.setSongName(s.getSongName());
            so.setArtistName(s.getArtistName());
            so.setAlbumName(s.getAlbumName());
            so.setAlbumId(s.getAlbumId());
            so.setSongId(s.getSongId());
            so.setData(s.getData());
            queue.add(so);
        }
        Song z = queue.get(1);
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        String.valueOf(z.getAlbumId()), Toast.LENGTH_SHORT);

        toast1.show();
    }

    public void createAleatory() {
        int n = songs.size();
        for(int i = 0; i < n; i++) {
            int r = auxiliarSongs.size();
            Random rand = new Random();
            int randomNum = rand.nextInt((r - 1) + 1) + 1;
            int ri = randomNum - 1;

            Song so = new Song();
            so.setSongName(auxiliarSongs.get(ri).getSongName());
            so.setArtistName(auxiliarSongs.get(ri).getArtistName());
            so.setAlbumName(auxiliarSongs.get(ri).getAlbumName());
            so.setAlbumId(auxiliarSongs.get(ri).getAlbumId());
            so.setSongId(auxiliarSongs.get(ri).getSongId());
            so.setData(auxiliarSongs.get(ri).getData());
            aleatorySongs.add(so);

            auxiliarSongs.remove(ri);

        }
    }


    public void createThread() {
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

    }

    public void createTimer() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                advancedSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0,1000);
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
            img.setImageResource(R.drawable.botonreproducir);
        }
        else {
            mediaPlayer.start();
            img.setImageResource(R.drawable.botonpausar);
        }
    }

    public void buttonNextClicked(View view) {
        nextSong();
    }

    public void buttonPreviousClicked(View view) {
        previousSong();
    }

    public void buttonRepeatClicked(View view) {
        repeat = !repeat;
        mediaPlayer.setLooping(repeat);
    }

    public void buttonAleatoryClicked(View view) {
        aleatory = !aleatory;
    }

    public void drawNewSong(ArrayList<Song> songss) {
        TextView tSong = findViewById(R.id.txtSongDetail);
        TextView tArtist = findViewById(R.id.txtArtistDetail);
        TextView tAlbum = findViewById(R.id.txtAlbumDetail);


        tSong.setText(songss.get(countSongs).getSongName());
        tArtist.setText(songss.get(countSongs).getArtistName());
        tAlbum.setText(songss.get(countSongs).getAlbumName());

    }

    public void nextSong() {


        if(countSongs < queue.size()) {
            countSongs++;
            if(countSongs < queue.size()) {

                mediaPlayer.reset();
                try {
                    if(aleatory){
                        mediaPlayer.setDataSource(aleatorySongs.get(countSongs).getData());
                        drawNewSong(aleatorySongs);
                    }
                    else{
                        mediaPlayer.setDataSource(queue.get(countSongs).getData());
                        drawNewSong(queue);
                    }

                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    configureSeekBar();

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            nextSong();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void previousSong() {
        if(countSongs >= 0) {
            countSongs--;
            if(countSongs >= 0) {

                mediaPlayer.reset();
                try {
                    if(aleatory){
                        mediaPlayer.setDataSource(aleatorySongs.get(countSongs).getData());
                        drawNewSong(aleatorySongs);
                    }
                    else{
                        mediaPlayer.setDataSource(queue.get(countSongs).getData());
                        drawNewSong(queue);
                    }

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    configureSeekBar();
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            nextSong();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




}
