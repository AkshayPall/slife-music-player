package com.example.akshaypall.slife;

/**
 * Created by Akshay Pall on 3/30/2016.
 */
public class Song {
    private long mId;
    private String mName;
    private String mArtist;
    private String mAlbum;

    public Song(long id, String name, String artist, String album) {
        mId = id;
        mName = name;
        mAlbum = album;
        mArtist = artist;
    }

    public long getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public String getmArtist() {
        return mArtist;
    }

    public String getmAlbum() {
        return mAlbum;
    }
}
