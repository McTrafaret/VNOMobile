package com.example.vnomobile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.OnCommand;
import com.example.vnolib.command.ascommands.SDPCommand;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServersListActivity extends AppCompatActivity {

    RecyclerView listOfServersView;
    Set<Integer> serverIndexSet; // needed to know which of indexes were added

    @OnCommand(SDPCommand.class)
    private void gotServerInfo(SDPCommand command) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Integer listIndex = command.getIndex();
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
        this.listOfServersView.setAdapter(new ListOfServersAdapter());
        this.listOfServersView.setLayoutManager(new LinearLayoutManager(this));
        try {
            ClientHandler.getClient().requestServers();
        } catch (Exception e) {
            log.error("ServersListActivity: {}", e.toString());
        }
        ClientHandler.getClient().subscribeToCommand(SDPCommand.class, this);

    }
}