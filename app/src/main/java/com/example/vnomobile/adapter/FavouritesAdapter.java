package com.example.vnomobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.Server;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.viewholder.ListOfServersViewHolder;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<ListOfServersViewHolder> {

    private List<Server> serverList;
    private OnServerEntryListener listener;

    public FavouritesAdapter(OnServerEntryListener listener, List<Server> serverList) {
        this.serverList = serverList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListOfServersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_server, parent, false);
        return new ListOfServersViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfServersViewHolder holder, int position) {
        holder.bind(serverList.get(position));
    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }
}
