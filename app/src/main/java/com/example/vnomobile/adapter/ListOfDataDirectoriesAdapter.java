package com.example.vnomobile.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vnomobile.R;
import com.example.vnomobile.resource.DataDirectoriesRepository;
import com.example.vnomobile.resource.DataDirectory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListOfDataDirectoriesAdapter extends RecyclerView.Adapter<ListOfDataDirectoriesAdapter.ListOfDirectoriesViewHolder> {

    private DataDirectoriesRepository repository;
    private OnDirectoryListener onDirectoryListener;

    public ListOfDataDirectoriesAdapter(DataDirectoriesRepository repository, OnDirectoryListener listener) {
        this.repository = repository;
        this.onDirectoryListener = listener;
    }

    @NonNull
    @Override
    public ListOfDirectoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_data_directory, parent, false);
        return new ListOfDirectoriesViewHolder(view, onDirectoryListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfDirectoriesViewHolder holder, int position) {
        holder.bind(repository.getDirectory(position));
    }

    @Override
    public int getItemCount() {
        return repository.getSize();
    }

    public class ListOfDirectoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView dataDirectoryIcon;
        private TextView dataDirectoryPath;
        private TextView clientName;
        private Resources resources;

        private DataDirectory directory;
        private OnDirectoryListener listener;

        public ListOfDirectoriesViewHolder(@NonNull View itemView, OnDirectoryListener listener) {
            super(itemView);
            this.resources = itemView.getContext().getResources();
            dataDirectoryIcon = itemView.findViewById(R.id.data_directory_icon);
            dataDirectoryPath = itemView.findViewById(R.id.path_text);
            clientName = itemView.findViewById(R.id.client_name);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(DataDirectory directory) {
            this.directory = directory;
            Glide.with(dataDirectoryIcon)
                    .load(directory.getMobileIconFile())
                    .error(R.drawable.vno_icon)
                    .into(dataDirectoryIcon);
            dataDirectoryPath.setText(directory.getPath());
            clientName.setText(directory.getName());
        }


        @Override
        public void onClick(View v) {
            listener.onDirectoryClick(directory);
        }
    }
}
