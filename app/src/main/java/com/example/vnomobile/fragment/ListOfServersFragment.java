package com.example.vnomobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.OnCommand;
import com.example.vnolib.client.model.Server;
import com.example.vnolib.command.ascommands.SDPCommand;
import com.example.vnolib.command.servercommands.PCCommand;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.LoadingActivity;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.ListOfServersAdapter;
import com.example.vnomobile.adapter.OnServerEntryListener;
import com.example.vnomobile.resource.LogHandler;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListOfServersFragment extends Fragment implements OnServerEntryListener {

    private Client client;
    private RecyclerView listOfServersView;
    private Set<Integer> serverIndexSet; // needed to know which of indexes were added

    public ListOfServersFragment() {
        // Required empty public constructor
    }

    @OnCommand(PCCommand.class)
    private void  connectedToServer(PCCommand command) {
        client.unsubscribeAll();
        LogHandler.create();
        Intent intent = new Intent(getActivity(), LoadingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @OnCommand(SDPCommand.class)
    private void gotServerInfo(SDPCommand command) {
        getActivity().runOnUiThread(new Runnable() {
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = ClientHandler.getClient();
        this.serverIndexSet = new HashSet<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_of_servers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.listOfServersView = view.findViewById(R.id.list_of_servers_view);
        this.listOfServersView.setAdapter(new ListOfServersAdapter(this));
        this.listOfServersView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        try {
            client.requestServers();
        } catch (Exception e) {
            log.error("ServersListActivity: {}", e.toString());
        }
        client.subscribeToCommand(SDPCommand.class, this);
    }

    @Override
    public void onServerEntryClick(Server server) {
        try {
            client.subscribeToCommand(PCCommand.class, this);
            client.connectToServer(server);
            client.disconnectFromMaster();
            client.unsubscribeFromCommand(SDPCommand.class, this);
        } catch (Exception ex) {
            log.error("Error while connecting to server:", ex);
        }
    }
}