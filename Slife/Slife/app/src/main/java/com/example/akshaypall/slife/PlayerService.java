package com.example.akshaypall.slife;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by rohitsharma on 2016-05-22.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    //media player
    private MediaPlayer mPlayer;
    //song list
    private ArrayList<Song> mSongs;
    //current position
    private int mSongPosition;

    private final IBinder mMusicBind = new PlayerBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        mSongPosition = 0; //initiate song position
        mPlayer = new MediaPlayer(); //prepare media player
        initMusicPlayer();
    }

    private void initMusicPlayer() {
        //to allow playback even when device is asleep
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        mPlayer.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //when player is ready to play song
        mp.start(); //play song
    }

    public void setSongs (ArrayList<Song> songs){
        mSongs = songs;
    }

    public void setASong(int songIndex){
        mSongPosition = songIndex;
    }

    public class PlayerBinder extends Binder {
        PlayerService getService() {
            return PlayerService.this;
        }
    }

    /**
     *
     * PLAYER METHODS BELOW
     *
     * **/

    public void playSong() {
        mPlayer.reset();

        //get song
        Song playSong = mSongs.get(mSongPosition);
        //get id
        long currSong = playSong.getmId();
        //set uri
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);
        try{
            mPlayer.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MUSIC SERVICE", "Error setting data source", e);
            Toast.makeText(getApplicationContext(), "Error playing song", Toast.LENGTH_SHORT).show();
        }

        mPlayer.prepareAsync();
    }
}
