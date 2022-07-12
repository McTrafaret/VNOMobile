package com.example.vnomobile.fragment;

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
import com.example.vnolib.command.servercommands.MCCommand;
import com.example.vnolib.command.servercommands.MSCommand;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.StringListAdapter;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.LogHandler;
import com.example.vnomobile.resource.ResourceHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFragment extends Fragment {

    private RecyclerView recyclerView;

    private Client client;
    private DataDirectory dataDirectory;
    private LogHandler logHandler;

    public LogFragment() {
        // Required empty public constructor
    }

    Runnable updateViewRunnable = new Runnable() {
        @Override
        public void run() {
            recyclerView.getAdapter().notifyItemInserted(logHandler.getIcLog().size() - 1);
        }
    };

    @OnCommand(MSCommand.class)
    public void onICMessage(MSCommand command) {
        getActivity().runOnUiThread(updateViewRunnable);
    }

    @OnCommand(MCCommand.class)
    public void onMusicCued(MCCommand command) {
        getActivity().runOnUiThread(updateViewRunnable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = ClientHandler.getClient();
        logHandler = LogHandler.getInstance();
        dataDirectory = ResourceHandler.getInstance().getDirectory();
        client.subscribeToCommand(MSCommand.class, this);
        client.subscribeToCommand(MCCommand.class, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.log_recycler_view);
        StringListAdapter adapter = new StringListAdapter(logHandler.getIcLog());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}