package com.example.vnomobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.OnCommand;
import com.example.vnolib.client.model.Server;
import com.example.vnolib.command.ascommands.SDPCommand;
import com.example.vnolib.command.servercommands.PCCommand;
import com.example.vnomobile.adapter.ListOfServersAdapter;
import com.example.vnomobile.adapter.OnServerEntryListener;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServersListActivity extends AppCompatActivity implements OnServerEntryListener {

    RecyclerView listOfServersView;
    Set<Integer> serverIndexSet; // needed to know which of indexes were added

    @OnCommand(PCCommand.class)
    private void  connectedToServer(PCCommand command) {
        ClientHandler.getClient().unsubscribeFromCommand(PCCommand.class, this);
        log.debug("Alive");
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
    }

    @OnCommand(SDPCommand.class)
    private void gotServerInfo(SDPCommand command) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int listIndex = command.getIndex();
                if(serverIndexSet.contains(listIndex)) {
                    listOfServersView.getAdapter().notifyItemChanged(listIndex);
                }
                else {
                    serverIndexSet.add(listIndex);
                    listOfServersView.getAdapter().notifyItemInserted(listIndex);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_list);
        this.serverIndexSet = new HashSet<>();
        this.listOfServersView = findViewById(R.id.list_of_servers_view);
        this.listOfServersView.setAdapter(new ListOfServersAdapter(this));
        this.listOfServersView.setLayoutManager(new LinearLayoutManager(this));
        try {
            ClientHandler.getClient().requestServers();
        } catch (Exception e) {
            log.error("ServersListActivity: {}", e.toString());
        }
        ClientHandler.getClient().subscribeToCommand(SDPCommand.class, this);

    }

    @Override
    public void onServerEntryClick(Server server) {
        try {
            ClientHandler.getClient().subscribeToCommand(PCCommand.class, this);
            ClientHandler.getClient().connectToServer(server);
            ClientHandler.getClient().disconnectFromMaster();
            ClientHandler.getClient().unsubscribeFromCommand(SDPCommand.class, this);
        } catch (Exception ex) {
            log.error("Error while connecting to server:", ex);
        }
    }
}