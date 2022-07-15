package com.example.vnomobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.OnCommand;
import com.example.vnolib.client.model.Area;
import com.example.vnolib.command.servercommands.ROOKCommand;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.AreaAdapter;

public class AreaFragment extends Fragment {

    private AreaAdapter areaAdapter;

    private RecyclerView areaRecyclerView;
    private SearchView searchView;
    private Button goButton;

    private Client client;

    private Area selectedArea = null;

    public AreaFragment() {
        // Required empty public constructor
    }

    @OnCommand(ROOKCommand.class)
    public void onChangeArea(ROOKCommand command) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int currentAreaInAdapter = areaAdapter.getCurrentArea();
                areaAdapter.changeCurrentArea(selectedArea.getLocationId() - 1);
                areaAdapter.notifyItemChanged(selectedArea.getLocationId() - 1);
                areaAdapter.notifyItemChanged(currentAreaInAdapter);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.areaAdapter = new AreaAdapter(ClientHandler.getClient().getAreas());
        this.client = ClientHandler.getClient();
        client.subscribeToCommand(ROOKCommand.class, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_area, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.areaRecyclerView = view.findViewById(R.id.area_recycler_view);
        this.areaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.areaRecyclerView.setAdapter(areaAdapter);
        this.searchView = view.findViewById(R.id.area_search);
        this.goButton = view.findViewById(R.id.area_go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedArea = areaAdapter.getSelectedArea();
                client.requestAreaChange(selectedArea);
            }
        });
    }
}