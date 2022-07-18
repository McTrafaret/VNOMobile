package com.example.vnomobile.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.Track;
import com.example.vnomobile.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> implements Filterable {

    private Track[] allTracks;
    private Track[] tracks;

    private int lastItemSelectedPosition = -1;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(constraint == null || constraint.toString().trim().isEmpty()) {
                filterResults.values = new ArrayList<Track>(Arrays.asList(allTracks));
                return filterResults;
            }

            List<Track> filteredResults = new ArrayList<>();

            for (Track track : allTracks) {
                if(track.getTrackName().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                    filteredResults.add(track);
                }
            }

            filterResults.values = filteredResults;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Track> filteredTracks = (List<Track>) results.values;
            tracks = filteredTracks.toArray(new Track[0]);
            notifyDataSetChanged();
        }
    };

    public MusicAdapter(Track[] tracks) {
        this.allTracks = tracks;
        this.tracks = this.allTracks;
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

    @Override
    public Filter getFilter() {
        return filter;
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
