package com.example.vnomobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListOfServersAdapter extends RecyclerView.Adapter<ListOfServersAdapter.ListOfServersViewHolder> {

    @NonNull
    @Override
    public ListOfServersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_server, parent, false);
        return new ListOfServersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfServersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ClientHandler.getClient().getServers().size();
    }

    public class ListOfServersViewHolder extends RecyclerView.ViewHolder {

        public ListOfServersViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
