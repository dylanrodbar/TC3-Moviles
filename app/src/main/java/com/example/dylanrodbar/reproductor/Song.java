package com.example.dylanrodbar.reproductor;

/**
 * Created by dylanrodbar on 14/3/2018.
 */

public class Song {
    private String songName;
    private String artistName;
    private String albumName;
    private String path;
    private String data;
    private long albumId;

    Song() {
    }

    public String getSongName() {
        return songName;
    }
    public long getAlbumId() {
        return albumId;
    }
    public String getArtistName() {
        return artistName;
    }
    public String getAlbumName() {
        return albumName;
    }
    public String getPath() {
        return path;
    }
    public String getData() {
        return data;
    }
    public void setSongName(String songName) {
        this.songName = songName;
    }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setData(String data) {
        this.data = data;
    }
    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
