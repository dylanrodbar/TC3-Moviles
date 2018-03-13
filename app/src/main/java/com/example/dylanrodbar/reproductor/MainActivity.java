package com.example.dylanrodbar.reproductor;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.dylanrodbar.cointransformer.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
