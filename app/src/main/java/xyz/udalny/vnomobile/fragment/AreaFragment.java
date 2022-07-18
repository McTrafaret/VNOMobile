package xyz.udalny.vnomobile.fragment;

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

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.client.model.Area;
import xyz.udalny.vnolib.command.servercommands.ROOKCommand;
import xyz.udalny.vnolib.command.servercommands.RaCCommand;
import xyz.udalny.vnolib.command.servercommands.RoCCommand;
import xyz.udalny.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import xyz.udalny.vnomobile.adapter.AreaAdapter;

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
                areaAdapter.changeCurrentArea(client.getCurrentArea());
            }
        });
    }

    @OnCommand(RaCCommand.class)
    public void areaInfoChanged(RaCCommand command) {
        Area area = client.getAreaById(command.getIdOfTheLocation());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                areaAdapter.areaInfoChanged(area);
            }
        });
    }

    @OnCommand(RoCCommand.class)
    public void twoAreasInfoChanged(RoCCommand command) {
        Area area1 = client.getAreaById(command.getIdOfTheJoinLocation());
        Area area2 = client.getAreaById(command.getIdOfTheLeaveLocation());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                areaAdapter.areaInfoChanged(area1);
                areaAdapter.areaInfoChanged(area2);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.areaAdapter = new AreaAdapter(ClientHandler.getClient().getAreas());
        this.client = ClientHandler.getClient();
        client.subscribeToCommand(ROOKCommand.class, this);
        client.subscribeToCommand(RoCCommand.class, this);
        client.subscribeToCommand(RaCCommand.class, this);
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
        this.searchView = view.findViewById(R.id.area_search);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                areaAdapter.getFilter().filter(newText);
                return false;
            }
        });
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

    @Override
    public void onDestroy() {
        client.unsubscribeFromCommand(ROOKCommand.class, this);
        client.unsubscribeFromCommand(RoCCommand.class, this);
        client.unsubscribeFromCommand(RaCCommand.class, this);
        super.onDestroy();
    }
}