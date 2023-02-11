package xyz.udalny.vnomobile.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import xyz.udalny.vnomobile.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.udalny.vnolib.client.model.Area;
import xyz.udalny.vnomobile.ClientHandler;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.ResourceHandler;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> implements Filterable {

    private Area[] allAreas;
    private Area[] areas;

    private int currentAreaId;
    private int lastItemSelectedPosition = -1;

    private DataDirectory dataDirectory;

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if(constraint == null || constraint.toString().trim().isEmpty()) {
                filterResults.values = new ArrayList<>(Arrays.asList(allAreas));
                return filterResults;
            }

            List<Area> filteredAreas = new ArrayList<>();

            for(Area area : allAreas) {
                if(area.getLocationName().toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                    filteredAreas.add(area);
                }
            }

            filterResults.values = filteredAreas;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Area> filteredAreas = (List<Area>) results.values;
            areas = filteredAreas.toArray(new Area[0]);
            notifyDataSetChanged();
        }
    };

    public AreaAdapter(Area[] areas) {
        this.allAreas = areas;
        this.areas = areas;
        this.currentAreaId = ClientHandler.getClient().getCurrentArea().getLocationId();
        this.dataDirectory = ResourceHandler.getInstance().getDirectory();
    }

    @NonNull
    @Override
    public AreaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_area, parent, false);
        return new AreaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaViewHolder holder, int position) {
        holder.bind(areas[position],
                position == lastItemSelectedPosition,
                areas[position].getLocationId() == currentAreaId);
    }

    public Area getSelectedArea() {
        if(lastItemSelectedPosition == -1) {
            return null;
        }
        return areas[lastItemSelectedPosition];
    }

    public void areaInfoChanged(Area newArea) {
        allAreas[newArea.getLocationId() - 1] = newArea;
        for(int i = 0; i < areas.length; i++) {
            if(areas[i].getLocationId() == newArea.getLocationId()) {
                areas[i] = newArea;
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void changeCurrentArea(Area area) {
        int prevAreaId = currentAreaId;
        currentAreaId = area.getLocationId();
        for(int i = 0; i < areas.length; i++) {
            if(areas[i].getLocationId() == currentAreaId) {
                notifyItemChanged(i);
            }
            if(areas[i].getLocationId() == prevAreaId) {
                notifyItemChanged(i);
            }
        }

    }

    public int getCurrentArea() {
        return currentAreaId;
    }

    @Override
    public int getItemCount() {
        return areas.length;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Area area;

        private final Resources resources;
        private final ConstraintLayout layout;
        private final TextView areaName;
        private final TextView characterCount;
        private final TextView youAreHereText;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);

            this.resources = itemView.getResources();
            this.layout = itemView.findViewById(R.id.area_item_layout);
            this.areaName = itemView.findViewById(R.id.area_name_text);
            this.characterCount = itemView.findViewById(R.id.character_count_text);
            this.youAreHereText = itemView.findViewById(R.id.you_are_here_text);
            itemView.setOnClickListener(this);
        }

        public void bind(Area area, boolean selected, boolean current) {
            File backgroundFile = dataDirectory.getBackgroundFile(area.getBackgroundNamePattern());
            if(backgroundFile != null) {
                Glide.with(layout.getContext())
                        .load(backgroundFile)
                        .error(R.drawable.saul_icon)
                        .into(new CustomTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                layout.setBackground(resource);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });
            }
            if(current) {
                youAreHereText.setVisibility(View.VISIBLE);
            }
            else {
                youAreHereText.setVisibility(View.GONE);
            }
            areaName.setText(area.getLocationName());
            characterCount.setText(String.format("%d", area.getLocationPopulation()));
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
