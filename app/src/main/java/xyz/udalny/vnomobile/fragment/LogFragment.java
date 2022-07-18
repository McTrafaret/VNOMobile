package xyz.udalny.vnomobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import xyz.udalny.vnomobile.adapter.StringListAdapter;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.ICLogListener;
import xyz.udalny.vnomobile.resource.LogHandler;
import xyz.udalny.vnomobile.resource.ResourceHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogFragment extends Fragment implements ICLogListener {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = ClientHandler.getClient();
        logHandler = LogHandler.getInstance();
        dataDirectory = ResourceHandler.getInstance().getDirectory();
        logHandler.subscribeToICLog(this);
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

    @Override
    public void onNewIcLogEntry() {
        getActivity().runOnUiThread(updateViewRunnable);
    }

    @Override
    public void onDestroy() {
        logHandler.unsubscribeFromICLog(this);
        super.onDestroy();
    }
}