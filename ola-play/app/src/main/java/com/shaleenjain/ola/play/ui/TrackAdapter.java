/*
 * Copyright (c) 2017. Shaleen Jain
 */

package com.shaleenjain.ola.play.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shaleenjain.ola.play.R;
import com.shaleenjain.ola.play.data.model.Track;
import com.shaleenjain.ola.play.injection.ActivityContext;
import com.shaleenjain.ola.play.utils.MediaIDHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shaleenjain.ola.play.utils.MediaIDHelper.MEDIA_ID_ALL;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private List<Track> mTracks;
    private Context mContext;
    private SearchResultsActivity mActivity;

    @Inject
    public TrackAdapter(@ActivityContext Context context, Activity activity) {
        mTracks = new ArrayList<>();
        mContext = context;
        mActivity = (SearchResultsActivity) activity;
    }

    public void setRibots(List<Track> tracks) {
        mTracks = tracks;
    }

    @Override
    public TrackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        return new TrackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrackViewHolder holder, int position) {
        Track track = mTracks.get(position);
        holder.nameTextView.setText(track.song());
        holder.artistTextView.setText(track.artists());
        holder.parent.setOnClickListener(v -> {

            String mediaID =  MediaIDHelper.createMediaID(
                    String.valueOf(track.url().hashCode()), MediaIDHelper.MEDIA_ID_ALL, MEDIA_ID_ALL);

            MediaControllerCompat.getMediaController(mActivity).getTransportControls()
                    .playFromMediaId(mediaID, null);

            mActivity.onItemClicked();
        });

        Glide.with(mContext)
                .load(track.cover_image())
                .centerCrop()
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {

        View parent;
        @BindView(R.id.cover_image)
        ImageView coverImage;
        @BindView(R.id.text_name) TextView nameTextView;
        @BindView(R.id.text_artist) TextView artistTextView;

        public TrackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            parent = itemView;
        }
    }

    public interface Callback {
        void onItemClicked();
    }
}
