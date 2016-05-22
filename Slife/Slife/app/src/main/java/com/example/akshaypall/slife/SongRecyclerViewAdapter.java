package com.example.akshaypall.slife;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.akshaypall.slife.SongFragment.OnSongPressedListener;

import java.util.List;

public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewAdapter.ViewHolder> {

    private final List<Song> mSongs;
    private final OnSongPressedListener mListener;

    public SongRecyclerViewAdapter(List<Song> songs, OnSongPressedListener listener) {
        mSongs = songs;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSong = mSongs.get(position);
        if (holder.mSong != null){
            char albumInitial = (holder.mSong.getmAlbum() != null) ? holder.mSong.getmAlbum().charAt(0) : 'U';
            holder.mAlbumInitial.setText(""+albumInitial);

            String songTitle = (holder.mSong.getmName() != null) ? holder.mSong.getmName() : "Unnamed Track";
            holder.mSongTitle.setText(songTitle);

            String artistName = (holder.mSong.getmArtist() != null) ? holder.mSong.getmArtist() : "Unknown Artist";
            holder.mArtistName.setText(artistName);

            //TODO: UPDATE SONG LENGTH LABEL
        } else {
            Log.wtf("Error", "holder.msong is NULL");
        }

        holder.mSongTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.wtf("should click!", "is song clicked?");
                if (mListener != null) {
                    Log.wtf("song pressed???",holder.mSong.getmName());
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSongPressed(holder.mSong);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAlbumInitial;
        public final TextView mSongTitle;
        public final TextView mArtistName;
        public final TextView mLength;
        public Song mSong;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAlbumInitial = (TextView) view.findViewById(R.id.song_album_initial);
            mSongTitle = (TextView) view.findViewById(R.id.song_title);
            mArtistName = (TextView)view.findViewById(R.id.song_artist);
            mLength = (TextView)view.findViewById(R.id.song_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSongTitle.getText() + "'";
        }
    }
}
