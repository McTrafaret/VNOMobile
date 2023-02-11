package xyz.udalny.vnomobile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.command.servercommands.ADCommand;
import xyz.udalny.vnolib.command.servercommands.CADCommand;
import xyz.udalny.vnolib.command.servercommands.MDCommand;
import xyz.udalny.vnomobile.R;

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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int percentage = loaded.get() * 100 / fullUnitsOfInformation;
                percentageInfo.setText(String.format("%s%%", percentage));
                progressBar.setProgress(percentage);
            }
        });
    }

    private void changeText(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingText.setText(text);
            }
        });
    }

    @OnCommand(CADCommand.class)
    private void onGetCharacter(CADCommand command) {
        loaded.addAndGet(command.getInfo().size());
        int numOfLoadedChars = loadedCharacters.addAndGet(command.getInfo().size());

        advanceProgress();

        if(client.getNumOfCharacters() == numOfLoadedChars) {
            client.unsubscribeFromCommand(CADCommand.class, this);
            changeText("Loading music...");
            client.requestTracks();
        }
    }

    @OnCommand(MDCommand.class)
    private void onGetTrack(MDCommand command) {
        loaded.incrementAndGet();
        int numOfLoadedTracks = loadedTracks.incrementAndGet();

        advanceProgress();

        if(client.getNumOfTracks() == numOfLoadedTracks) {
            client.unsubscribeFromCommand(MDCommand.class, this);
            changeText("Loading areas...");
            client.requestAreas();
        }

    }

    @OnCommand(ADCommand.class)
    private void onGetArea(ADCommand command) {
        loaded.incrementAndGet();
        int numOfLoadedAreas = loadedAreas.incrementAndGet();

        advanceProgress();

        if(client.getNumOfAreas() == numOfLoadedAreas) {
            client.unsubscribeFromCommand(ADCommand.class, this);
            changeText("Done!");
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

        client.requestCharacters();
        changeText("Loading characters...");
    }
}