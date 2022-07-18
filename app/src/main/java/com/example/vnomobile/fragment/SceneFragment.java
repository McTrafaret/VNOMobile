package com.example.vnomobile.fragment;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vnolib.client.Client;
import com.example.vnolib.client.OnCommand;
import com.example.vnolib.client.model.CharacterState;
import com.example.vnolib.command.servercommands.MSCommand;
import com.example.vnolib.command.servercommands.ROOKCommand;
import com.example.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.R;
import com.example.vnomobile.adapter.SpriteButtonsAdapter;
import com.example.vnomobile.render.Engine;
import com.example.vnomobile.resource.CharacterData;
import com.example.vnomobile.resource.CharacterIni;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.ResourceHandler;
import com.example.vnomobile.resource.SoundHandler;
import com.example.vnomobile.resource.UIDesign;
import com.example.vnomobile.util.UIUtil;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
    private ImageView positionButton;
    private ImageView flipButton;
    private ImageView sfxButton;

    private Client client;
    private DataDirectory dataDirectory;
    private CharacterState state;

    private CharacterData characterData;
    private CharacterIni currentIniFile;

    private String[] backgroundNames;
    private ArrayAdapter<String> backgroundAdapter;
    private SpriteButtonsAdapter buttonsAdapter;

    private Wini settings;
    private UIDesign design;

    private Engine sceneHandlerEngine;

    private String sfxName = "";
    private boolean sfxSelected = false;
    private File[] sfxFiles;

    @OnCommand(ROOKCommand.class)
    public void onAreaChanged(ROOKCommand command) {
        backgroundNames = dataDirectory.getBackgroundNames(client.getCurrentArea().getBackgroundNamePattern());
        state.setBackgroundName(client.getCurrentArea().getBackgroundNamePattern());
        sceneHandlerEngine.clearPositions();
        sceneHandlerEngine.showBackground(state.getBackgroundName());
        SoundHandler.getInstance().shutUp();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backgroundAdapter.notifyDataSetChanged();
                backgroundAdapter.clear();
                backgroundAdapter.addAll(backgroundNames);
            }
        });
    }

    @OnCommand(MSCommand.class)
    public void onICMessage(MSCommand command) {
        if(command.getBackgroundImageName() == null || command.getBackgroundImageName().isEmpty()) {
            command.setBackgroundImageName(client.getCurrentArea().getBackgroundNamePattern());
        }
        sceneHandlerEngine.handle(command);
    }

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
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
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

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            File file = getItem(position);

            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, null);
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
            this.currentIniFile = new CharacterIni(characterData.getIniFiles()[0]);
            this.settings = dataDirectory.getSettings();
            String designName = settings.get("DesignStyle", "design");
            this.design = dataDirectory.getDesign(designName);
            this.backgroundNames = dataDirectory.getBackgroundNames(client.getCurrentArea().getBackgroundNamePattern());
        } catch (Exception ex) {
            log.error("OnCreate: ", ex);
        }

        state.setBackgroundName(client.getCurrentArea().getBackgroundNamePattern());
        state.setSpriteName("1");
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

        this.sceneHandlerEngine = new Engine(sceneView, dataDirectory, design);

        initSendButton(view);
        initListOfCharacterButtons(view);
        initColorSlider(view);
        initBackgroundSpinner(view);
        initIniSpinner(view);
        initSfxSelectSpinner(view);
        initPositionButton(view);
        initFlipButton(view);
        initSfxButton(view);

        this.client.subscribeToCommand(MSCommand.class, this);
        sceneHandlerEngine.showBackground(state.getBackgroundName());

        this.client.subscribeToCommand(ROOKCommand.class, this);
    }

    @Override
    public void onDestroy() {
        sceneHandlerEngine.stop();
        client.unsubscribeFromCommand(MSCommand.class, this);
        client.unsubscribeFromCommand(ROOKCommand.class, this);
        super.onDestroy();
    }

    private void initSendButton(View view) {
        this.sendButton = view.findViewById(R.id.send_character_message_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageInput.getText().toString();
                state.setSfx(sfxSelected ? sfxName : "");
                client.sendICMessage(state, message);
                messageInput.setText("");
            }
        });
    }

    private void initListOfCharacterButtons(View view) {
        this.listOfCharacterButtons = view.findViewById(R.id.list_of_character_buttons);
        buttonsAdapter = new SpriteButtonsAdapter(currentIniFile.getButtons(), design, state);
        this.listOfCharacterButtons.setAdapter(buttonsAdapter);
        this.listOfCharacterButtons.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initColorSlider(View view) {
        this.colorsSlider = view.findViewById(R.id.colors_slider);
        colorsSlider.setMax(6);
        colorsSlider.setProgress(0);
        colorsSlider.setOnSeekBarChangeListener(new ColorSliderListener(state));
    }

    private void initBackgroundSpinner(View view) {
        this.backgroundSelectSpinner = view.findViewById(R.id.background_select_spinner);

        this.backgroundAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>(Arrays.asList(backgroundNames)));

        backgroundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.backgroundSelectSpinner.setAdapter(backgroundAdapter);
        this.backgroundSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state.setBackgroundName(backgroundNames[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSfxSelectSpinner(View view) {
        this.sfxSelectSpinner = view.findViewById(R.id.sfx_select_spinner);
        this.sfxFiles = dataDirectory.getSfxFiles();
        FileArrayAdapter adapter = new FileArrayAdapter(getContext(), android.R.layout.simple_spinner_item, sfxFiles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sfxSelectSpinner.setAdapter(adapter);
        sfxSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sfxName = sfxFiles[position].getName().split("\\.")[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initIniSpinner(View view) {
        this.iniSelectSpinner = view.findViewById(R.id.ini_select_spinner);
        FileArrayAdapter adapter = new FileArrayAdapter(getContext(), android.R.layout.simple_spinner_item, characterData.getIniFiles());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iniSelectSpinner.setAdapter(adapter);
        iniSelectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                File selectedIniFile = characterData.getIniFiles()[position];
                try {
                    currentIniFile = new CharacterIni(selectedIniFile);
                    buttonsAdapter.setButtons(currentIniFile.getButtons());
                    buttonsAdapter.notifyDataSetChanged();
                } catch (IOException e) {
                    log.error("IniSelectSpinner onItemSelected: ", e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initPositionButton(View view) {
        this.positionButton = view.findViewById(R.id.position_button);
        Glide.with(this)
                .load(design.getPositionToFileMap().get(state.getPosition()))
                .into(positionButton);
        this.positionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpritePosition newPosition = state.getPosition().nextPosition();
                state.setPosition(newPosition);
                Glide.with(SceneFragment.this)
                        .load(design.getPositionToFileMap().get(newPosition))
                        .into(positionButton);
            }
        });
    }

    private void initFlipButton(View view) {

        this.flipButton = view.findViewById(R.id.flip_button);
        Glide.with(this)
                .load(design.getFlipToFileMap().get(state.getFlip()))
                .into(flipButton);
        this.flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpriteFlip newFlip = state.getFlip().nextFlip();
                state.setFlip(newFlip);
                Glide.with(SceneFragment.this)
                        .load(design.getFlipToFileMap().get(newFlip))
                        .into(flipButton);
            }
        });
    }

    private void initSfxButton(View view) {
        this.sfxButton = view.findViewById(R.id.sfx_button);
        Glide.with(this)
                .load(design.getSfxButtonsFiles()[sfxSelected ? 1 : 0])
                .into(sfxButton);
        this.sfxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfxSelected = !sfxSelected;
                Glide.with(SceneFragment.this)
                        .load(design.getSfxButtonsFiles()[sfxSelected ? 1 : 0])
                        .into(sfxButton);
            }
        });

    }


}