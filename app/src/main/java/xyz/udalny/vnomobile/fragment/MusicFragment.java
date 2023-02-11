package xyz.udalny.vnomobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.model.Track;
import xyz.udalny.vnolib.command.servercommands.enums.LoopingStatus;
import xyz.udalny.vnomobile.ClientHandler;
import xyz.udalny.vnomobile.R;
import xyz.udalny.vnomobile.adapter.MusicAdapter;

public class MusicFragment extends Fragment {

    private SearchView musicSearchView;
    private MusicAdapter musicAdapter;
    private RecyclerView musicRecyclerView;
    private CheckBox loopCheckbox;
    private Button playButton;

    private Client client;

    public MusicFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = ClientHandler.getClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.musicSearchView = view.findViewById(R.id.music_search_view);
        musicSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                musicAdapter.getFilter().filter(newText);
                return false;
            }
        });
        this.musicRecyclerView = view.findViewById(R.id.music_recycler_view);
        this.musicAdapter = new MusicAdapter(client.getTracks());
        this.musicRecyclerView.setAdapter(musicAdapter);
        this.musicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.loopCheckbox = view.findViewById(R.id.loop_checkbox);
        this.playButton = view.findViewById(R.id.music_play_button);
        this.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track track = musicAdapter.getSelectedTrack();
                if(track != null) {
                    client.playTrack(track, LoopingStatus.fromBoolean(loopCheckbox.isChecked()));
                }
            }
        });
    }
}