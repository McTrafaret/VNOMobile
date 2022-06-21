package com.example.vnomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.OnCommand;
import com.example.vnolib.client.model.Character;
import com.example.vnolib.command.ascommands.NoCommand;
import com.example.vnolib.command.servercommands.AllowedCommand;
import com.example.vnomobile.adapter.RosterImageAdapter;
import com.example.vnomobile.exception.ResourceNotFoundException;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.ResourceHandler;

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

    @OnCommand(NoCommand.class)
    public void onCharacterNotAllowed(NoCommand command) {
        Toast.makeText(this, "This character is already in use", Toast.LENGTH_SHORT).show();
    }

    @OnCommand(AllowedCommand.class)
    public void onCharacterAllowed(AllowedCommand command) {
        client.unsubscribeFromCommand(AllowedCommand.class, this);
        client.unsubscribeFromCommand(NoCommand.class, this);
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
                Bitmap bigArt = null;
                try {
                    bigArt = dataDirectory.getBigArt(selectedCharacter.getCharName());
                } catch (ResourceNotFoundException e) {
                    log.warn("Trying to get BigArt: {}", e);
                }
                if(bigArt == null) {
                    bigArt = BitmapFactory.decodeResource(getResources(), R.drawable.saul_icon);
                }
                bigArtImage.setImageBitmap(bigArt);
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

        client.subscribeToCommand(AllowedCommand.class, this);
        client.subscribeToCommand(NoCommand.class, this);
    }
}