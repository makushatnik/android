package com.softdesign.vkmusic.ui.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.softdesign.vkmusic.R;
import com.softdesign.vkmusic.data.managers.DataManager;
import com.softdesign.vkmusic.data.model.Song;
import com.softdesign.vkmusic.ui.adapters.SongsAdapter;
import com.softdesign.vkmusic.utils.AudioPlayer;
import com.softdesign.vkmusic.utils.Common;
import com.softdesign.vkmusic.utils.ConstantManager;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ageev Evgeny on 27.07.2016.
 */
public class MainFragment extends Fragment {
    private static final String TAG = ConstantManager.TAG_PREFIX + "MainFragment";
    private List<Song> mSongs;
    private SongsAdapter mSongsAdapter;
    private RecyclerView mRecyclerView;
    private int mCurrentSong;
    private int mChoiceMode;

    private RelativeLayout mControls;
    private ImageView mPlay, mPrev, mNext, mShuffle, mRepeat;
    private TextView mDuration, mProgress;
    private SeekBar mSeekBar;
    private AudioPlayer mPlayer;
    private boolean mLooping;

    //private int mPos;
    private String mQuery;
    private boolean myAudio;

    private DataManager mDataManager;

    public static MainFragment newInstance(String query, boolean isMy, int pos) {
        MainFragment fragment = new MainFragment();

        Bundle args = new Bundle();
        //args.putSerializable(ConstantManager.SONG_LIST, songList);
        args.putInt(ConstantManager.SONG_POSITION, pos);
        args.putBoolean(ConstantManager.MY_AUDIO, isMy);
        args.putString(ConstantManager.SEARCH_QUERY, query);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle saved) {
        super.onCreate(saved);
        setRetainInstance(true);

        Bundle args = getArguments();
        if (args != null) {
            mCurrentSong = args.getInt(ConstantManager.SONG_POSITION, 0);
            myAudio = args.getBoolean(ConstantManager.MY_AUDIO, true);
            mQuery = args.getString(ConstantManager.SEARCH_QUERY, "null");
        }

        mSongs = new ArrayList<>();
        mPlayer = new AudioPlayer();
        mDataManager = DataManager.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle saved) {
        View v = inflater.inflate(R.layout.fragment_main, parent, false);

        View emptyView = v.findViewById(R.id.empty_music_list);
        mSongsAdapter = new SongsAdapter(mSongs, new SongsAdapter.SongViewHolder.CustomClickListener() {
            @Override
            public void onSongClickListener(int pos) {
                mCurrentSong = pos;
            }
        }, emptyView, mChoiceMode);

        mRecyclerView = (RecyclerView)v.findViewById(R.id.recview_music);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSongsAdapter);

        mControls = (RelativeLayout)v.findViewById(R.id.controls);

        mPlay = (ImageView)v.findViewById(R.id.play);
        mPrev = (ImageView)v.findViewById(R.id.prev);
        mNext = (ImageView)v.findViewById(R.id.next);
        mShuffle = (ImageView)v.findViewById(R.id.shuffle);
        mRepeat = (ImageView)v.findViewById(R.id.repeat);

        mPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prev();
            }
        });
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        mShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shuffle();
            }
        });
        mRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeat();
            }
        });

        mDuration = (TextView)v.findViewById(R.id.duration);
        mProgress = (TextView)v.findViewById(R.id.progress);
        mSeekBar = (SeekBar)v.findViewById(R.id.seekbar);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        searchByQuery();

        if (mSongs.size() == 0) {
            mControls.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    public void setSearchParams(String query, boolean isMy) {
        mQuery = query;
        myAudio = isMy;
    }

    private void searchByQuery() {
        String query = mQuery;

        VKParameters params = new VKParameters();
        params.put("q", query);
        params.put("auto_complete", 1);
        params.put("performer_only", 0);//by author
        params.put("sort", 2);//2 - popular, 1 - duration, 0 - date
        params.put("search_own", (myAudio ? 1 : 0));

        //params.put("count", 3);//limit the count
        Log.d(TAG, "myAudio = " + myAudio);
        //Log.d(TAG, "2 - " + (myAudio ? 1 : 0));

        VKRequest request = VKApi.audio().search(params);

        mSongs.clear();
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);

                String tmp;
                for (int i = 0; i < ((VKList<VKApiAudio>) response.parsedModel).size(); i++) {
                    VKApiAudio vkApiAudio = ((VKList<VKApiAudio>) response.parsedModel).get(i);

                    Song song = new Song();
                    song.setId(vkApiAudio.id);
                    tmp = vkApiAudio.title;
                    tmp = (tmp.length() > 50 ? tmp.substring(0, 49) : tmp);
                    song.setName(tmp);
                    tmp = vkApiAudio.artist;
                    tmp = (tmp.length() > 25 ? tmp.substring(0, 24) : tmp);
                    song.setAuthor(vkApiAudio.artist);
                    song.setAlbumId(vkApiAudio.album_id);
                    song.setDuration(vkApiAudio.duration);
                    song.setUrl(vkApiAudio.url);

                    mSongs.add(song);
                }

                mSongsAdapter.receiveData(mSongs);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                Log.d(TAG, "Failed: " + request + ", " + attemptNumber + ", " + totalAttempts);
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                Log.d(TAG, "Error: " + error.errorCode + ", " + error.errorMessage + ", " + error.errorReason);
                super.onError(error);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }

    //region controls

    public void play() {
        if (mPlayer.isPlaying()) {
            pause();
            return;
        }

        if (mSongs.size() == 0) {
            Log.d(TAG, "Не найдено ни одной композиции!");
            return;
        }

        if (mCurrentSong < 0 || mCurrentSong > mSongs.size()) mCurrentSong = 0;

        mPlayer.play(getActivity(), Uri.parse(mSongs.get(mCurrentSong).getUrl()), mLooping);
        mPlay.setImageResource(R.drawable.pauza);
    }

    public void pause() {
        mPlayer.pause();
        mPlay.setImageResource(R.drawable.play);
    }

    public void next() {
        if (mCurrentSong > mSongs.size()) return;

        mCurrentSong++;
        play();
    }

    public void prev() {
        if (mCurrentSong == 0) return;

        mCurrentSong--;
        play();
    }

    public void shuffle() {

    }

    public void repeat() {
        mLooping = !mLooping;
        mPlayer.setLooping(mLooping);
    }

    //end region

    private void downloadSong(String title, String url) {
        if (url == null) return;

        if (!Common.isExternalStorageWritable()) return;

        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getPath();
            File fileOut = new File(path + "/" + title);
            File fileIn = new File(url);
        //} catch (IOException e) {

        } catch (Exception e) {

        }
    }
}
