package com.example.vnomobile.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.Area;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.ResourceHandler;

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
            Bitmap backgroundBitmap = dataDirectory.getBackground(area.getBackgroundNamePattern());
            if(backgroundBitmap != null) {
                Drawable drawable = new BitmapDrawable(resources, backgroundBitmap);
                layout.setBackground(drawable);
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
