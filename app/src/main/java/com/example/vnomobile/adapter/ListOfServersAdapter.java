package com.example.vnomobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.Server;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListOfServersAdapter extends RecyclerView.Adapter<ListOfServersAdapter.ListOfServersViewHolder> {

    private OnServerEntryListener listener;

    public ListOfServersAdapter(OnServerEntryListener listener) {
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
        Server server = ClientHandler.getClient().getServers().get(position);
        holder.bind(server);
    }

    @Override
    public int getItemCount() {
        return ClientHandler.getClient().getServers().size();
    }

    public class ListOfServersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView serverName;
        private TextView serverDescription;

        private TextView serverIp;
        private TextView serverPort;

        private Server server;
        private OnServerEntryListener listener;

        public ListOfServersViewHolder(@NonNull View itemView, OnServerEntryListener listener) {
            super(itemView);
            this.serverName = itemView.findViewById(R.id.server_name);
            this.serverDescription = itemView.findViewById(R.id.server_description);
            this.serverIp = itemView.findViewById(R.id.server_ip);
            this.serverPort = itemView.findViewById(R.id.server_port);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(Server server) {
            this.server = server;
            serverName.setText(server.getName());
            serverDescription.setText(server.getDescription());
            serverIp.setText(server.getIp());
            serverPort.setText(Integer.toString(server.getPort()));
        }

        @Override
        public void onClick(View v) {
            log.debug("Clicked");
            listener.onServerEntryClick(server);
        }
    }
}
