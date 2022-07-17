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
import com.example.vnolib.command.servercommands.PCCommand;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.LoadingActivity;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.FavouritesAdapter;
import com.example.vnomobile.adapter.OnServerEntryListener;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.LogHandler;
import com.example.vnomobile.resource.ResourceHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FavouritesFragment extends Fragment implements OnServerEntryListener {

    private DataDirectory dataDirectory;
    private Client client;
    private RecyclerView recyclerView;

    public FavouritesFragment() {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dataDirectory = ResourceHandler.getInstance().getDirectory();
        this.client = ClientHandler.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list_of_servers_view);
        recyclerView.setAdapter(new FavouritesAdapter(this, dataDirectory.getFavourites()));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public void onServerEntryClick(Server server) {
        try {
            client.subscribeToCommand(PCCommand.class, this);
            client.connectToServer(server);
            client.disconnectFromMaster();
        } catch (Exception ex) {
            log.error("Error while connecting to server:", ex);
        }
    }
}