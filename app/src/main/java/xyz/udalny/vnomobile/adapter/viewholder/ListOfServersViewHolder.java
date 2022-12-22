package xyz.udalny.vnomobile.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnomobile.R;

import lombok.extern.slf4j.Slf4j;
import xyz.udalny.vnolib.client.model.Server;
import xyz.udalny.vnomobile.adapter.OnServerEntryListener;

@Slf4j
public class ListOfServersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView serverName;
    private final TextView serverDescription;

    private final TextView serverIp;
    private final TextView serverPort;

    private Server server;
    private final OnServerEntryListener listener;

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
        listener.onServerEntryClick(server);
    }
}
