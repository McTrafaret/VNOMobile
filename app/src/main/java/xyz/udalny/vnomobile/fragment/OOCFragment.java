package xyz.udalny.vnomobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import xyz.udalny.vnomobile.adapter.StringListAdapter;
import xyz.udalny.vnomobile.resource.LogHandler;
import xyz.udalny.vnomobile.resource.OOCLogListener;

public class OOCFragment extends Fragment implements OOCLogListener {

    private RecyclerView messageListRecyclerView;
    private EditText messageInput;
    private Button sendButton;

    private Client client;

    private LogHandler logHandler;

    Runnable updateViewRunnable = new Runnable() {
        @Override
        public void run() {
            messageListRecyclerView.getAdapter().notifyItemInserted(logHandler.getOocLog().size() - 1);
        }
    };

    public OOCFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.logHandler = LogHandler.getInstance();
        this.client = ClientHandler.getClient();
        this.logHandler.subscribeToOOCLog(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ooc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageListRecyclerView = view.findViewById(R.id.ooc_recycler_view);
        StringListAdapter adapter = new StringListAdapter(logHandler.getOocLog());
        messageListRecyclerView.setAdapter(adapter);
        messageListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        messageInput = view.findViewById(R.id.ooc_edit_text);
        sendButton = view.findViewById(R.id.ooc_send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                if(!message.isEmpty()) {
                    client.sendOOCMessage(message);
                    messageInput.setText("");
                }
            }
        });
    }

    @Override
    public void onNewOOCLogEntry() {
        getActivity().runOnUiThread(updateViewRunnable);
    }

    @Override
    public void onDestroy() {
        logHandler.unsubscribeFromOOCLog(this);
        super.onDestroy();
    }
}