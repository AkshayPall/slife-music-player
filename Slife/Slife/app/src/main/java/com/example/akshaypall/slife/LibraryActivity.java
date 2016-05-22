package com.example.akshaypall.slife;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LibraryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SongFragment.OnSongPressedListener {

    public static ArrayList<Song> SONGS_LIST;

    public static final float MEDIA_PLAYER_HEIGHT = 165;

    BottomSheetLayout bottomSheetLayout;

    private PlayerService mPlayerService;
    private Intent mPlayIntent;
    private boolean mMusicBound = false;

    //current song views
    private TextView mCurrentSongTitle;
    private TextView mCurrentSongArtist;
    private ImageView mCurrentSongThumbnail; //TODO: update to use
    private ImageButton mCurrentSongStateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //for the setup of the tabs for SONGS, ALBUM, and ARTISTS
        ViewPager viewPager = (ViewPager)findViewById(R.id.library_viewpager);
        viewPager.setOffscreenPageLimit(3);
        setLibraryViewPager(viewPager);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.library_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //load songs
        SONGS_LIST = getSongs();

        //setup of NEW flipboard bottomsheet
        bottomSheetLayout = (BottomSheetLayout) findViewById(R.id.bottomsheet);
        bottomSheetLayout.setOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener() {
            @Override
            public void onSheetStateChanged(BottomSheetLayout.State state) {
                if (state != null){
                    if (state == BottomSheetLayout.State.HIDDEN)
                        bottomSheetLayout.peekSheet();
                }
            }
        });
        bottomSheetLayout.setPeekSheetTranslation(MEDIA_PLAYER_HEIGHT);
        bottomSheetLayout.setInterceptContentTouch(false);

        bottomSheetLayout.showWithSheetView(getLayoutInflater().inflate(R.layout.medoa_bottomsheet, bottomSheetLayout, false));

        setupCurrentSongViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mPlayIntent==null){
            mPlayIntent = new Intent(this, PlayerService.class);
            bindService(mPlayIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(mPlayIntent);
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.PlayerBinder binder = (PlayerService.PlayerBinder)service;
            //get service
            mPlayerService = binder.getService();
            //pass list
            mPlayerService.setSongs(SONGS_LIST);
            mMusicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMusicBound = false;
        }
    };

    public ArrayList<Song> getSongs() {
        ArrayList<Song> songs = new ArrayList<>();
        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);

                String thisArtist;
                if (musicCursor.getString(artistColumn) != null)
                    thisArtist = musicCursor.getString(artistColumn);
                else {
                    thisArtist = "Unknown Artist";
                }


                String thisAlbum;
                if (musicCursor.getString(albumColumn) != null)
                    thisAlbum = musicCursor.getString(albumColumn);
                else {
                    thisAlbum = "Unknown Album";
                }

                Log.wtf("Title: ", thisTitle+", by "+thisArtist+", on :"+ thisAlbum);
                songs.add(new Song(thisId, thisTitle, thisArtist, thisAlbum));
            }
            while (musicCursor.moveToNext());
        }
        return songs;
    }

    private void setupCurrentSongViews() {
        mCurrentSongTitle = (TextView)findViewById(R.id.current_song_name);
        mCurrentSongArtist =(TextView)findViewById(R.id.current_song_artist);
        mCurrentSongStateButton = (ImageButton)findViewById(R.id.bottomsheet_play_pause_button);
        mCurrentSongThumbnail = (ImageView)findViewById(R.id.current_song_thumbnail);
    }

    private void setLibraryViewPager (ViewPager viewPager){
        LibraryViewPagerAdapter adapter = new LibraryViewPagerAdapter(getSupportFragmentManager());
        //TODO: finish creating fragment classes
        //TODO: remove test frags
        adapter.addFragment(new SongFragment(), getString(R.string.library_song));
        adapter.addFragment(new Fragment(), getString(R.string.library_album));
        adapter.addFragment(new Fragment(), getString(R.string.library_artist));
//        adapter.addFragment(new AlbumFragment(), R.string.library_album);
//        adapter.addFragment(new ArtistFragment(), R.string.library_artist);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.library, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_library) {
            // Handle the camera action
        } else if (id == R.id.nav_people) {

        } else if (id == R.id.nav_trending) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSongPressed(Song song) {
        //TODO: update media player
        Log.wtf("Song selected", "" + song.getmName());
    }

    //viewpager adapter class

    private class LibraryViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public LibraryViewPagerAdapter (FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return  mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment (Fragment frag, String title) {
            mFragmentList.add(frag);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
