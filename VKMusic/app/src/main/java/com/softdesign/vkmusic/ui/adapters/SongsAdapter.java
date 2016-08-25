package com.softdesign.vkmusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softdesign.vkmusic.R;
import com.softdesign.vkmusic.data.model.Song;
import com.softdesign.vkmusic.utils.Common;

import java.util.List;

/**
 * Created by Ageev Evgeny on 27.07.2016.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    private Context mContext;
    private List<Song> mSongs;
    private final View mEmptyView;
    //private final ItemChoiceManager mICM;

    private SongViewHolder.CustomClickListener mListener;

    public SongsAdapter(List<Song> songs,
                        SongViewHolder.CustomClickListener listener,
                        View emptyView,
                        int choiceMode) {
        mSongs = songs;
        mListener = listener;
        mEmptyView = emptyView;
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
//        mICM = new ItemChoiceManager(this);
//        mICM.setChoiceMode(choiceMode);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_song_list, parent, false);
        return new SongViewHolder(convertView, mListener);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int pos) {
        Song song = mSongs.get(pos);

        holder.mAuthor.setText(song.getAuthor());
        holder.mTitle.setText(song.getName());
        holder.mDuration.setText(Common.getDurationAsString(song.getDuration()));

        if (Common.fileExists(song.getName())) {
            holder.mDownload.setImageDrawable(mContext.getResources().getDrawable(R.drawable.load));
        }
    }

    @Override
    public int getItemCount() { return mSongs.size(); }

    public void receiveData() {
        Log.d("SongsAdapter", "Songs count - " + getItemCount());
        notifyDataSetChanged();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    public void receiveData(List<Song> newList) {
        mSongs = newList;
        notifyDataSetChanged();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView mAuthor, mTitle;
        protected ImageView mDownload;
        protected TextView mDuration;

        private CustomClickListener mListener;

        public SongViewHolder(View itemView, CustomClickListener listener) {
            super(itemView);
            mListener = listener;

            mAuthor = (TextView)itemView.findViewById(R.id.author_txt);
            mTitle = (TextView)itemView.findViewById(R.id.title_txt);
            mDuration = (TextView)itemView.findViewById(R.id.duration_txt);
            mDownload = (ImageView)itemView.findViewById(R.id.download_iv);
            mDownload.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onSongClickListener(getAdapterPosition());
            }
        }

        public interface CustomClickListener {
            void onSongClickListener(int pos);
        }
    }
}
