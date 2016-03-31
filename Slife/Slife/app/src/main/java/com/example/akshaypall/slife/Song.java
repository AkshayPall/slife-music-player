package com.example.akshaypall.slife;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Akshay Pall on 3/30/2016.
 */
public class Song {
//    private static Uri MUSIC_URI= android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

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

    //TODO: implement duration measuring song length based on indivual uri
//    private String setTime () {
//        // load data file
//        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
//        metaRetriever.setDataSource(MUSIC_URI.toString());
//
//        String out = "";
//        // get mp3 info
//
//        // convert duration to minute:seconds
//        String duration =
//                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
//        Log.v("time", duration);
//        long dur = Long.parseLong(duration);
//        String seconds = String.valueOf((dur % 60000) / 1000);
//
//        Log.v("seconds", seconds);
//        String minutes = String.valueOf(dur / 60000);
//        out = minutes + ":" + seconds;
//        if (seconds.length() == 1) {
//            txtTime.setText("0" + minutes + ":0" + seconds);
//        }else {
//            txtTime.setText("0" + minutes + ":" + seconds);
//        }
//        Log.v("minutes", minutes);
//        // close object
//        metaRetriever.release();
//
//        return minutes+":"+seconds;
//    }
}
