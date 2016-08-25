package com.softdesign.vkmusic.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softdesign.vkmusic.R;

import java.util.List;

/**
 * Created by Ageev Evgeny on 25.08.2016.
 */
public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder> {
    private Context mContext;
    private List<String> mSavedSongs;
    private final View mEmptyView;

    public SavedAdapter(List<String> savedSongs, View emptyView, int choiceMode) {
        mSavedSongs = savedSongs;
        mEmptyView = emptyView;
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public SavedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_saved_list, parent, false);
        return new SavedViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(SavedViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mSavedSongs.size();
    }

    public void receiveData() {
        Log.d("SongsAdapter", "Saved songs count - " + getItemCount());
        notifyDataSetChanged();
        if (mEmptyView != null) {
            mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }

    public static class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public SavedViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
