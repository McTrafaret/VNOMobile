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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.client.model.Server;
import xyz.udalny.vnolib.command.ascommands.SDPCommand;
import xyz.udalny.vnolib.command.servercommands.PCCommand;
import xyz.udalny.vnolib.exception.ConnectionException;
import xyz.udalny.vnomobile.ClientHandler;
import xyz.udalny.vnomobile.LoadingActivity;
import com.example.vnomobile.R;
import xyz.udalny.vnomobile.adapter.ListOfServersAdapter;
import xyz.udalny.vnomobile.adapter.OnServerEntryListener;
import xyz.udalny.vnomobile.resource.LogHandler;

import java.util.HashSet;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListOfServersFragment extends Fragment implements OnServerEntryListener {

    private Client client;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView listOfServersView;
    private Set<Integer> serverIndexSet; // needed to know which of indexes were added

    boolean subscribedToServersInfo = false;

    public ListOfServersFragment() {
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

    @OnCommand(SDPCommand.class)
    private void gotServerInfo(SDPCommand command) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int listIndex = command.getIndex();
                if (serverIndexSet.contains(listIndex)) {
                    listOfServersView.getAdapter().notifyItemChanged(listIndex);
                } else {
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
        this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        this.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                if (!client.connectedToMaster()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), R.string.not_connected_to_master, Toast.LENGTH_SHORT);
                        }
                    });
                    return;
                }
                log.debug("Servers: {}", client.getServers());
                int currentNumOfServers = client.getServers().size();
                for(int i = 0; i < currentNumOfServers; i++) {
                    listOfServersView.getAdapter().notifyItemRemoved(i);
                }
                try {
                    client.requestServers();
                } catch (ConnectionException e) {
                    log.error("ServersListFragment: {}", e.toString());
                }
            }
        });
        this.listOfServersView = view.findViewById(R.id.list_of_servers_view);
        this.listOfServersView.setAdapter(new ListOfServersAdapter(this));
        this.listOfServersView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        if (client.connectedToMaster()) {
            try {
                client.requestServers();
            } catch (Exception e) {
                log.error("ServersListFragment: {}", e.toString());
            }
            client.subscribeToCommand(SDPCommand.class, this);
            subscribedToServersInfo = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!subscribedToServersInfo) {
            client.subscribeToCommand(SDPCommand.class, this);
            subscribedToServersInfo = true;
        }
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
                    client.subscribeToCommand(PCCommand.class, ListOfServersFragment.this);
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
                client.unsubscribeFromCommand(SDPCommand.class, ListOfServersFragment.this);
                subscribedToServersInfo = false;
            }
        }).start();
    }
}