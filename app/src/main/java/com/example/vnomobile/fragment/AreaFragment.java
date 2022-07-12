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

import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.AreaAdapter;

public class AreaFragment extends Fragment {

    private RecyclerView areaRecyclerView;
    private SearchView searchView;
    private Button goButton;

    public AreaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        this.areaRecyclerView.setAdapter(new AreaAdapter(ClientHandler.getClient().getAreas()));
        this.searchView = view.findViewById(R.id.area_search);
        this.goButton = view.findViewById(R.id.area_go_button);
    }
}