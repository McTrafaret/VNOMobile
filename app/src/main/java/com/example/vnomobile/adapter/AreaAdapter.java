package com.example.vnomobile.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.vnolib.client.model.Area;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.ResourceHandler;

import java.io.File;

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.AreaViewHolder> {

    private Area[] areas;

    private int currentAreaIndex;
    private int lastItemSelectedPosition = -1;

    private DataDirectory dataDirectory;

    public AreaAdapter(Area[] areas) {
        this.areas = areas;
        this.currentAreaIndex = ClientHandler.getClient().getCurrentArea().getLocationId() - 1;
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
                position == currentAreaIndex);
    }

    @Override
    public int getItemCount() {
        return areas.length;
    }

    public class AreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Area area;

        private Resources resources;
        private LinearLayout layout;
        private TextView areaName;
        private TextView youAreHereText;

        public AreaViewHolder(@NonNull View itemView) {
            super(itemView);

            this.resources = itemView.getResources();
            this.layout = itemView.findViewById(R.id.area_item_layout);
            this.areaName = itemView.findViewById(R.id.area_name_text);
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
