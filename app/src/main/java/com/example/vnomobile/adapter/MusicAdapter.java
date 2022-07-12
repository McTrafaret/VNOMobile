package com.example.vnomobile.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.Track;
import com.example.vnomobile.R;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private Track[] tracks;

    private int lastItemSelectedPosition = -1;

    public MusicAdapter(Track[] tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_music_track, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        holder.bind(tracks[position], position == lastItemSelectedPosition);
    }

    @Override
    public int getItemCount() {
        return tracks.length;
    }

    public Track getSelectedTrack() {
        if (lastItemSelectedPosition == -1) {
            return null;
        }
        return tracks[lastItemSelectedPosition];
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LinearLayout layout;
        private TextView trackName;
        private Track track;

        private Resources resources;

        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            resources = itemView.getResources();
            layout = itemView.findViewById(R.id.music_item_layout);
            trackName = itemView.findViewById(R.id.music_name_text);
            itemView.setOnClickListener(this);
        }

        public void bind(Track track, boolean selected) {
            this.track = track;
            this.trackName.setText(this.track.getTrackName());
            layout.setBackgroundColor(selected ? resources.getColor(R.color.blue) : resources.getColor(R.color.white));
        }

        @Override
        public void onClick(View v) {
            int saveLastPos = lastItemSelectedPosition;
            lastItemSelectedPosition = getAdapterPosition();
            notifyItemChanged(saveLastPos);
            notifyItemChanged(lastItemSelectedPosition);
        }
    }
}
