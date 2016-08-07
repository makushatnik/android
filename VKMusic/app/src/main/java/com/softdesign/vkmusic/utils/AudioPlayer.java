package com.softdesign.vkmusic.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * Created by Ageev Evgeny on 01.08.2016.
 */
public class AudioPlayer {
    private MediaPlayer mPlayer;
    private boolean mPlaying;
    private Uri mSelectedUri;

    public boolean isPlaying() {
        return mPlaying;
    }

    public void setPlaying(boolean playing) {
        mPlaying = playing;
    }

    public void play(Context c, Uri uri, boolean loop) {
        if (mPlayer == null || mSelectedUri != uri) {
            mPlayer = MediaPlayer.create(c, uri);
            //mPlayer.prepareAsync();
            //mPlayer.prepare();
            if (loop) {
                mPlayer.setLooping(loop);
            } else {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        stop();
                        mPlaying = false;
                    }
                });
            }
            mSelectedUri = uri;
        }
        mPlayer.start();
        mPlaying = true;
    }

    public void stop() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
            mPlaying = false;
        }
    }

    public void pause() {
        if (!mPlaying) return;

        mPlayer.pause();
        mPlaying = false;
    }

    public void seekTo(int time) {
        if (mPlayer == null) return;

        if (time < 0 || time >= mPlayer.getDuration()) time = 0;
        mPlayer.seekTo(time);
    }

    public void setLooping(boolean b) {
        if (mPlayer == null) return;

        mPlayer.setLooping(b);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {}
        });
    }
}
