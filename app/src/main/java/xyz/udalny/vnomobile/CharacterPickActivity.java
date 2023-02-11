package xyz.udalny.vnomobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.client.model.Character;
import xyz.udalny.vnolib.command.ascommands.NoCommand;
import xyz.udalny.vnolib.command.servercommands.AllowedCommand;
import xyz.udalny.vnolib.command.servercommands.TKNCommand;
import xyz.udalny.vnomobile.R;

import xyz.udalny.vnomobile.adapter.RosterImageAdapter;
import xyz.udalny.vnomobile.exception.ResourceNotFoundException;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.ResourceHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CharacterPickActivity extends AppCompatActivity {

    private TextView characterNameTextView;
    private ImageView bigArtImage;
    private GridView rosterImageGrid;
    private EditText passwordInput;
    private Button pickCharacterButton;

    private Character selectedCharacter;

    private DataDirectory dataDirectory;
    private Client client;

    boolean subscribed = false;

    @OnCommand(TKNCommand.class)
    public void onCharacterNotAllowed(TKNCommand command) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CharacterPickActivity.this, "This character is already in use", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnCommand(AllowedCommand.class)
    public void onCharacterAllowed(AllowedCommand command) {
        client.unsubscribeFromCommand(AllowedCommand.class, this);
        client.unsubscribeFromCommand(NoCommand.class, this);
        subscribed = false;
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_pick);

        selectedCharacter = null;
        dataDirectory = ResourceHandler.getInstance().getDirectory();
        client = ClientHandler.getClient();

        characterNameTextView = findViewById(R.id.character_name_text_view);
        bigArtImage = findViewById(R.id.bigArt);
        rosterImageGrid = findViewById(R.id.character_pick_grid);
        passwordInput = findViewById(R.id.character_password_field);
        pickCharacterButton = findViewById(R.id.pick_character_button);

        rosterImageGrid.setAdapter(new RosterImageAdapter(dataDirectory));
        rosterImageGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCharacter = client.getCharacterByIndex(position);
                try {
                    Glide.with(CharacterPickActivity.this)
                                    .load(dataDirectory.getBigArtFile(selectedCharacter))
                                    .error(R.drawable.saul_icon)
                                    .into(bigArtImage);
                } catch (ResourceNotFoundException e) {
                    log.error("While setting bigart:", e);
                }
                characterNameTextView.setText(selectedCharacter.getCharName());
            }
        });

        pickCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedCharacter == null) {
                    Toast.makeText(getApplicationContext(), "Please pick the character", Toast.LENGTH_SHORT).show();
                    return;
                }
                String password = passwordInput.getText().toString();
                client.pickCharacter(selectedCharacter, password);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!subscribed) {
            client.subscribeToCommand(AllowedCommand.class, this);
            client.subscribeToCommand(TKNCommand.class, this);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    client.disconnectFromServer();
                    client.connectToMaster();
                } catch (Exception ex) {
                    log.error("When disconnecting to server and connecting to master: ", ex);
                }
            }
        }).start();
        finish();
    }
}