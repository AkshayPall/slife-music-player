package com.example.akshaypall.slife;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnSongPressedListener}
 * interface.
 */
public class SongFragment extends Fragment{

    //parameter argument names
    private static final String ARG_TAB_NAME = "tab_name";
    //possible parameters
    public static final String SONG_TAB = "Song";
    public static final String ALBUM_TAB = "Album";
    public static final String ARTIST_TAB = "Artist";

    private OnSongPressedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongFragment() {
    }

    //parameter initialization
    @SuppressWarnings("unused")
    public static SongFragment newInstance(String tabName) {
        SongFragment fragment = new SongFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TAB_NAME, tabName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            String mTabName = getArguments().getString(ARG_TAB_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_song_list, container, false);

        //QUERY THE SONGS
        ArrayList<Song> mSongList = LibraryActivity.SONGS_LIST;
        Collections.sort(mSongList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getmName().compareTo(b.getmName());
            }
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new SongRecyclerViewAdapter(mSongList, mListener));
        }

        return view;
    }




//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(getActivity());
//        if (context instanceof OnSongPressedListener) {
//            mListener = (OnSongPressedListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnSongPressedListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSongPressedListener {
        void onSongPressed (Song song);
    }
}
