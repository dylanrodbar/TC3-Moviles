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
import android.media.TimedText;
import android.net.Uri;
import android.os.Handler;
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

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class MusicDetailActivity extends AppCompatActivity  {

    MediaPlayer mediaPlayer;
    AudioManager audioManager;
    ArrayList<Song> songs;
    ArrayList<Song> auxiliarSongs;
    ArrayList<Song> aleatorySongs;
    ArrayList<Song> queue;
    ArrayList<LRCSong> lrcSong;
    private boolean repeat = false;
    private boolean aleatory = false;
    SeekBar advancedSeekBar;
    int countSongs = 0;
    Timer timer;
    String songL;
    int n = 0;
    int max = 0;
    boolean songLyrics = false;
    Thread tr;
    long tId = 0;
    private static Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        boolean pause = false;
        String song = getIntent().getStringExtra("song");
        songL = song;
        String artist = getIntent().getStringExtra("artist");
        String album = getIntent().getStringExtra("album");
        String path = getIntent().getStringExtra("path");
        String data = getIntent().getStringExtra("data");
        songs = new ArrayList<Song>();
        lrcSong = new ArrayList<LRCSong>();
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




        Bitmap bm = BitmapFactory.decodeFile(path);

        //Uri artworkUri = Uri.parse("content://media/external/audio/albumart");
        //Uri path1 = ContentUris.withAppendedId(artworkUri, aID);
        //Uri uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/artpop");
        Glide.with(getApplicationContext()).load(bm).apply(new RequestOptions().circleCrop()).into(image);
        //Glide.with(image.getContext()).load(path1).into(image);
        //image.setImageBitmap(bm);



        setLRCToSong();
        max = lrcSong.size();
        createThread();
        createThreadLRC();
        createTimer();






    }


        public void setLRCToSong() {
            TextView t = findViewById(R.id.textViewLyrics);
            lrcSong.clear();
            String a = songL.toLowerCase();
            String b = a.replace(" ","");
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/raw/"+b);
            InputStream inputStream = null;
            String str = "";
           try {
               inputStream = getContentResolver().openInputStream(uri);

           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }

           if(inputStream == null) songLyrics = false;
           else {
               songLyrics = true;
               BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
               boolean first = true;
               String cont = "";
               String dura = "00:00.00";
               long duraLrc = convertTime(dura);
               LRCSong l = new LRCSong(cont, duraLrc);
               lrcSong.add(l);
               try {
                   while((str = in.readLine())!=null){
                       String duration = str.substring(1, 9);
                       String fin = str.substring(10, str.length());
                       long lrcDuration = convertTime(duration);
                       LRCSong lr = new LRCSong(fin, lrcDuration);
                       long prevLrcDuration = lrcSong.get(lrcSong.size() - 1).getDuration();
                       long prevSleepDuration = timeOfLine(prevLrcDuration, lrcDuration);

                       lrcSong.get(lrcSong.size()-1).setSleepDuration(prevSleepDuration);
                       lrcSong.add(lr);


                   }
                   lrcSong.get(lrcSong.size() - 1).setSleepDuration(0);


               } catch (IOException e) {
                   e.printStackTrace();
               }
           }





   }

   public long timeOfLine(long time1, long time2) {
        return time2 - time1;

   }

   public long convertTime(String time) {
       time = time.replace('.', ':');
       String[] times = time.split(":");
       // mm:ss:SS
       return Integer.valueOf(times[0]) * 60 * 1000 +
               Integer.valueOf(times[1]) * 1000 +
               Integer.valueOf(times[2]) ;
   }

    public void setAlbumImage() {
        Bitmap bm;
        ImageView image = findViewById(R.id.imageView2);
        if(aleatory){
            bm = BitmapFactory.decodeFile(aleatorySongs.get(countSongs).getPath());

        }
        else{
            bm = BitmapFactory.decodeFile(queue.get(countSongs).getPath());
        }


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
            so.setPath(s.getPath());
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
            so.setPath(s.getPath());
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
            so.setPath(auxiliarSongs.get(ri).getPath());
            aleatorySongs.add(so);

            auxiliarSongs.remove(ri);

        }
    }


    public void createThread() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                tr = Thread.currentThread();

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

    public void drawTextLyrics(String text) {
        TextView t = findViewById(R.id.textViewLyrics);
        t.setText(text);
    }

    public void createThreadLRC() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Thread tr1 = Thread.currentThread();
                tId = tr1.getId();
                //int n = lrcSong.size();
                //int i = 0;
                long time = 0;
                String text = "";
                String text1 = "";
                while (true) {
                    if(songLyrics) {
                        if (!mediaPlayer.isPlaying()) {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        else {
                            try {
                                time = lrcSong.get(n).getSleepDuration();
                                text = lrcSong.get(n).getContent();
                                Thread.sleep(time);
                                text1 = lrcSong.get(n+1).getContent();
                                n++;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }





                    final long finalTime = time;
                    final String finalText = text1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            drawTextLyrics(finalText);
                            try {
                                Thread.sleep(10);
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
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                advancedSeekBar.setProgress(mediaPlayer.getCurrentPosition());
            }
        },0, 1000);
    }




    public void drawSongRotation() {
        if(mediaPlayer.isPlaying()) {
            ImageView relativeSong = findViewById(R.id.imageView2);
            ImageButton imgV = findViewById(R.id.playSongButton);
            float deg = relativeSong.getRotation() + 70F;
            relativeSong.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
        }

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
        ImageButton im = findViewById(R.id.repeatSongButton);
        repeat = !repeat;
        //mediaPlayer.setLooping(repeat);
        if(repeat == true) {
            im.setImageResource(R.drawable.botonrepeticions);
        }
        else {
            im.setImageResource(R.drawable.botonrepeticion);
        }
    }

    public void buttonAleatoryClicked(View view) {
        ImageButton im = findViewById(R.id.aleatorySongButton);
        aleatory = !aleatory;
        if(aleatory == true) {
            im.setImageResource(R.drawable.botonaleatorios);
        }
        else {
            im.setImageResource(R.drawable.botonaleatorio);
        }
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
            if(!repeat) countSongs++;
            if(countSongs < queue.size()) {

                mediaPlayer.reset();
                try {

                    if(aleatory){
                        mediaPlayer.setDataSource(aleatorySongs.get(countSongs).getData());
                        songL = aleatorySongs.get(countSongs).getSongName();
                        drawNewSong(aleatorySongs);
                    }
                    else{
                        mediaPlayer.setDataSource(queue.get(countSongs).getData());
                        songL = queue.get(countSongs).getSongName();
                        drawNewSong(queue);
                    }

                    mediaPlayer.prepare();
                    mediaPlayer.start();


                    configureSeekBar();

                    setAlbumImage();


                    this.n = 0;
                    setLRCToSong();


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
            if(!repeat) countSongs--;
            if(countSongs >= 0) {

                mediaPlayer.reset();
                try {

                    if(aleatory){
                        mediaPlayer.setDataSource(aleatorySongs.get(countSongs).getData());
                        songL = aleatorySongs.get(countSongs).getSongName();
                        drawNewSong(aleatorySongs);
                    }
                    else{
                        mediaPlayer.setDataSource(queue.get(countSongs).getData());
                        songL = queue.get(countSongs).getSongName();
                        drawNewSong(queue);
                    }

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    configureSeekBar();

                    setAlbumImage();
                    this.n = 0;
                    setLRCToSong();

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
