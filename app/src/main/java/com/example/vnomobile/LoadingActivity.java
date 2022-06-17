package com.example.vnomobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.OnCommand;
import com.example.vnolib.command.servercommands.ADCommand;
import com.example.vnolib.command.servercommands.CADCommand;
import com.example.vnolib.command.servercommands.MDCommand;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoadingActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView percentageInfo;
    private TextView loadingText;

    // sum of number of characters, number of tracks and number of locations
    private int fullUnitsOfInformation;

    private AtomicInteger loaded;

    private AtomicInteger loadedCharacters;
    private AtomicInteger loadedTracks;
    private AtomicInteger loadedAreas;


    private Client client;

    private void advanceProgress() {
        int percentage = loaded.get() * 100 / fullUnitsOfInformation;
        percentageInfo.setText(String.format("%s%%", percentage));
        progressBar.setProgress(percentage);
    }

    @OnCommand(CADCommand.class)
    private void onGetCharacter(CADCommand command) {
        loaded.incrementAndGet();
        int numOfLoadedChars = loadedCharacters.incrementAndGet();

        advanceProgress();

        if(client.getNumOfCharacters() == numOfLoadedChars) {
            client.unsubscribeFromCommand(CADCommand.class, this);
            loadingText.setText("Loading music...");
            while(true) {
                try {
                    client.requestTracks();
                    break;
                } catch (InterruptedException e) {
                    log.warn("Interrupted while requesting tracks");
                }
            }
        }
    }

    @OnCommand(MDCommand.class)
    private void onGetTrack(MDCommand command) {
        loaded.incrementAndGet();
        int numOfLoadedTracks = loadedTracks.incrementAndGet();

        advanceProgress();

        if(client.getNumOfTracks() == numOfLoadedTracks) {
            client.unsubscribeFromCommand(MDCommand.class, this);
            loadingText.setText("Loading areas...");
            while(true) {
                try {
                    client.requestAreas();
                    break;
                } catch (InterruptedException e) {
                    log.warn("Interrupted while requesting areas");
                }
            }
        }

    }

    @OnCommand(ADCommand.class)
    private void onGetArea(ADCommand command) {
        loaded.incrementAndGet();
        int numOfLoadedAreas = loadedAreas.incrementAndGet();

        advanceProgress();

        if(client.getNumOfAreas() == numOfLoadedAreas) {
            client.unsubscribeFromCommand(ADCommand.class, this);
            progressBar.setProgress(100);
            loadingText.setText("Done!");
            Intent intent = new Intent(this, CharacterPickActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        this.progressBar = findViewById(R.id.progressBar);
        this.percentageInfo = findViewById(R.id.loading_percent_text);
        this.loadingText = findViewById(R.id.loading_info_text);

        progressBar.setProgress(0);
        this.client = ClientHandler.getClient();
        client.subscribeToCommand(CADCommand.class, this);
        client.subscribeToCommand(MDCommand.class, this);
        client.subscribeToCommand(ADCommand.class, this);

        this.fullUnitsOfInformation = client.getNumOfAreas() + client.getNumOfCharacters() + client.getNumOfTracks();
        this.loaded = new AtomicInteger(0);

        this.loadedCharacters = new AtomicInteger(0);
        this.loadedAreas = new AtomicInteger(0);
        this.loadedTracks = new AtomicInteger(0);

        while(true) {
            try {
                client.requestCharacters();
                break;
            } catch (InterruptedException e) {
                log.warn("Interrupted while requesting characters");
            }
        }

        loadingText.setText("Loading characters...");
    }
}