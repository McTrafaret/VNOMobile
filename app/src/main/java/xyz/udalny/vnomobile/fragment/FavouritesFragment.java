package xyz.udalny.vnomobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.client.model.Server;
import xyz.udalny.vnolib.command.servercommands.PCCommand;
import xyz.udalny.vnomobile.ClientHandler;
import xyz.udalny.vnomobile.LoadingActivity;
import com.example.vnomobile.R;
import xyz.udalny.vnomobile.adapter.FavouritesAdapter;
import xyz.udalny.vnomobile.adapter.OnServerEntryListener;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.LogHandler;
import xyz.udalny.vnomobile.resource.ResourceHandler;

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
    private void connectedToServer(PCCommand command) {
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

    public void onConnectToServerFailed() {
        client.unsubscribeFromCommand(PCCommand.class, this);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), R.string.server_connect_failed, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onServerEntryClick(Server server) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.subscribeToCommand(PCCommand.class, FavouritesFragment.this);
                    client.connectToServer(server);
                } catch (Exception ex) {
                    log.error("While connecting to server: ", ex);
                    onConnectToServerFailed();
                    return;
                }
                try {
                    client.disconnectFromMaster();
                } catch (Exception ex) {
                    log.error("Failed to disconnect from master: ", ex);
                }
            }
        }).start();
    }
}