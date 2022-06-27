package com.example.vnomobile.fragment;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.BlendModeColorFilterCompat;
import androidx.core.graphics.BlendModeCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.model.BoxName;
import com.example.vnolib.client.model.CharacterState;
import com.example.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.exception.ResourceNotFoundException;
import com.example.vnomobile.resource.CharacterData;
import com.example.vnomobile.resource.CharacterIni;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.ResourceHandler;
import com.example.vnomobile.util.UIUtil;

import java.io.File;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SceneFragment extends Fragment {

    private SurfaceView sceneView;
    private EditText messageInput;
    private Button sendButton;
    private RecyclerView listOfCharacterButtons;
    private SeekBar colorsSlider;
    private Spinner backgroundSelectSpinner;
    private Spinner iniSelectSpinner;
    private Spinner sfxSelectSpinner;
    private ImageButton positionButton;
    private ImageButton flipButton;
    private ImageButton sfxButton;

    private Client client;
    private DataDirectory dataDirectory;
    private CharacterState state;

    private CharacterData characterData;
    private CharacterIni currentIniFile;

    private static class ColorSliderListener implements SeekBar.OnSeekBarChangeListener {

        CharacterState state;

        public ColorSliderListener(CharacterState state) {
            this.state = state;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int colorId = UIUtil.getColorId(MessageColor.fromInt(progress));
            int color = seekBar.getResources().getColor(colorId);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                seekBar.getThumb().setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            }
            else {
                seekBar.getThumb().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
            state.setMessageColor(MessageColor.fromInt(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private static class FileArrayAdapter extends ArrayAdapter<File> {

        public FileArrayAdapter(@NonNull Context context, int resource, @NonNull File[] objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            File file = getItem(position);

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, null);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(file.getName());
            return textView;
        }

    }

    public SceneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.client = ClientHandler.getClient();
        this.state = new CharacterState();
        this.dataDirectory = ResourceHandler.getInstance().getDirectory();
        try {
            this.characterData = dataDirectory.getCharacterData(client.getCurrentCharacter());
        } catch (Exception ex) {
            log.error("OnCreate: ", ex);
        }

        state.setBackgroundName(client.getCurrentArea().getBackgroundNamePattern());
        state.setSpriteName("1"); // TODO: take values from characters directory
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scene, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.sceneView = view.findViewById(R.id.scene_view);
        this.messageInput = view.findViewById(R.id.character_message_input);


        this.sendButton = view.findViewById(R.id.send_character_message_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                client.sendICMessage(state, message);
            }
        });

        this.listOfCharacterButtons = view.findViewById(R.id.list_of_character_buttons);

        this.colorsSlider = view.findViewById(R.id.colors_slider);
        colorsSlider.setMax(6);
        colorsSlider.setProgress(0);
        colorsSlider.setOnSeekBarChangeListener(new ColorSliderListener(state));

        this.backgroundSelectSpinner = view.findViewById(R.id.background_select_spinner);

        this.iniSelectSpinner = view.findViewById(R.id.ini_select_spinner);
        FileArrayAdapter adapter = new FileArrayAdapter(getContext(), android.R.layout.simple_spinner_item, characterData.getIniFiles());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iniSelectSpinner.setAdapter(adapter);

        this.sfxSelectSpinner = view.findViewById(R.id.sfx_select_spinner);
        this.positionButton = view.findViewById(R.id.position_button);
        this.flipButton = view.findViewById(R.id.flip_button);
        this.sfxButton = view.findViewById(R.id.sfx_button);
    }
}