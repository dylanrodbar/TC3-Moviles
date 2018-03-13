package com.example.dylanrodbar.reproductor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MusicListActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.dylanrodbar.cointransformer.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        ListView list;
        String[] artistName ={
                "Lady Gaga",
                "Lady Gaga",
                "Lady Gaga",
        };

        String[] songName ={
                "Applause",
                "Alejandro",
                "Judas",
        };

        Integer[] imgid={
                R.drawable.artpop,
                R.drawable.tfm,
                R.drawable.btw,
        };

        AdapterListView adapter=new AdapterListView(this, artistName, songName, imgid);
        list=(ListView)findViewById(R.id.songList);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                //String Slecteditem= itemname[+position];
                //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                openMusicDetail();

            }
        });
    }

    public void openMusicDetail() {
        Intent intent = new Intent(this, MusicDetailActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "");
        startActivity(intent);
    }
}
