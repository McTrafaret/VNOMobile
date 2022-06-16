package com.example.vnomobile.adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnomobile.R;
import com.example.vnomobile.resource.DataDirectoriesRepository;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.util.FileUtil;

import java.io.File;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListOfDataDirectoriesAdapter extends RecyclerView.Adapter<ListOfDataDirectoriesAdapter.ListOfDirectoriesViewHolder> {

    @NonNull
    @Override
    public ListOfDirectoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_data_directory, parent, false);
        return new ListOfDirectoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfDirectoriesViewHolder holder, int position) {
        holder.bind(DataDirectoriesRepository.getInstance().getDirectory(position));
    }

    @Override
    public int getItemCount() {
        return DataDirectoriesRepository.getInstance().getSize();
    }

    public class ListOfDirectoriesViewHolder extends RecyclerView.ViewHolder {

        private ImageView dataDirectoryIcon;
        private TextView dataDirectoryPath;
        private Resources resources;

        public ListOfDirectoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            this.resources = itemView.getContext().getResources();
            dataDirectoryIcon = itemView.findViewById(R.id.data_directory_icon);
            dataDirectoryPath = itemView.findViewById(R.id.path_text);
        }

        public void bind(DataDirectory directory) {
            Bitmap bitmap = directory.getMobileIcon();
            if(bitmap == null) {
                bitmap = BitmapFactory.decodeResource(resources, R.drawable.vno_icon);
            }
            dataDirectoryIcon.setImageBitmap(bitmap);
            dataDirectoryPath.setText(directory.getPath());
        }
    }
}
