package com.example.vnomobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.model.Server;

import java.nio.charset.StandardCharsets;

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
        Server server = ClientHandler.getClient().getServers().get(position);
        holder.bind(server);
    }

    @Override
    public int getItemCount() {
        return ClientHandler.getClient().getServers().size();
    }

    public class ListOfServersViewHolder extends RecyclerView.ViewHolder {

        private TextView serverName;
        private TextView serverDescription;

        private TextView serverIp;
        private TextView serverPort;

        public ListOfServersViewHolder(@NonNull View itemView) {
            super(itemView);
            this.serverName = itemView.findViewById(R.id.server_name);
            this.serverDescription = itemView.findViewById(R.id.server_description);
            this.serverIp = itemView.findViewById(R.id.server_ip);
            this.serverPort = itemView.findViewById(R.id.server_port);
        }

        public void bind(Server server) {
            serverName.setText(server.getName());
            serverDescription.setText(server.getDescription());
            serverIp.setText(server.getIp());
            serverPort.setText(Integer.toString(server.getPort()));
        }
    }
}
