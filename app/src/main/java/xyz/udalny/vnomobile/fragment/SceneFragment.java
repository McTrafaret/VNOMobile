package xyz.udalny.vnomobile.fragment;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import xyz.udalny.vnomobile.R;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.OnCommand;
import xyz.udalny.vnolib.client.model.BoxName;
import xyz.udalny.vnolib.client.model.CharacterState;
import xyz.udalny.vnolib.command.servercommands.FORCESTREAMResponseCommand;
import xyz.udalny.vnolib.command.servercommands.MSCommand;
import xyz.udalny.vnolib.command.servercommands.ROOKCommand;
import xyz.udalny.vnolib.command.servercommands.SERVURLCommand;
import xyz.udalny.vnolib.command.servercommands.enums.MessageColor;
import xyz.udalny.vnolib.command.servercommands.enums.SpriteFlip;
import xyz.udalny.vnolib.command.servercommands.enums.SpritePosition;
import xyz.udalny.vnomobile.ClientHandler;
import xyz.udalny.vnomobile.adapter.SpriteButtonsAdapter;
import xyz.udalny.vnomobile.render.Engine;
import xyz.udalny.vnomobile.resource.CharacterData;
import xyz.udalny.vnomobile.resource.CharacterIni;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.ResourceHandler;
import xyz.udalny.vnomobile.resource.SoundHandler;
import xyz.udalny.vnomobile.resource.design.UIDesign;
import xyz.udalny.vnomobile.util.UIUtil;

@Slf4j
public class SceneFragment extends Fragment {

    private Menu menu;

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
        if (command.getBackgroundImageName() == null || command.getBackgroundImageName().isEmpty()) {
            command.setBackgroundImageName(client.getCurrentArea().getBackgroundNamePattern());
        }
        sceneHandlerEngine.handle(command);
    }

    @OnCommand(FORCESTREAMResponseCommand.class)
    public void onForceStream(FORCESTREAMResponseCommand command) {

    }

    @OnCommand(SERVURLCommand.class)
    public void onServUrl(SERVURLCommand command) {
        log.debug("Got servurl: {}", command);
        sceneHandlerEngine.showUrl(command.getUrl());
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                seekBar.getThumb().setColorFilter(new BlendModeColorFilter(color, BlendMode.SRC_ATOP));
            } else {
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

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, null);
            }
            TextView textView = convertView.findViewById(android.R.id.text1);
            textView.setText(file.getName());
            return textView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            File file = getItem(position);

            if (convertView == null) {
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
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.scene_menu, menu);
        this.menu = menu;
        switch (state.getBoxName()) {
            case USERNAME:
                menu.findItem(R.id.use_username_checkbox).setChecked(true);
                break;
            case MYSTERYNAME:
                menu.findItem(R.id.use_mysteryname_checkbox).setChecked(true);
                break;
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.use_mysteryname_checkbox:
                if(item.isChecked()) {
                    item.setChecked(false);
                    state.setBoxName(BoxName.CHARACTER_NAME);
                }
                else {
                    menu.findItem(R.id.use_username_checkbox).setChecked(false);
                    item.setChecked(true);
                    state.setBoxName(BoxName.MYSTERYNAME);
                }
                break;
            case R.id.use_username_checkbox:
                if(item.isChecked()) {
                    item.setChecked(false);
                    state.setBoxName(BoxName.CHARACTER_NAME);
                }
                else {
                    menu.findItem(R.id.use_mysteryname_checkbox).setChecked(false);
                    item.setChecked(true);
                    state.setBoxName(BoxName.USERNAME);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
        this.client.subscribeToCommand(SERVURLCommand.class, this);
        this.client.subscribeToCommand(FORCESTREAMResponseCommand.class, this);
    }

    @Override
    public void onDestroy() {
        sceneHandlerEngine.stop();
        client.unsubscribeFromCommand(MSCommand.class, this);
        client.unsubscribeFromCommand(ROOKCommand.class, this);
        this.client.unsubscribeFromCommand(SERVURLCommand.class, this);
        this.client.unsubscribeFromCommand(FORCESTREAMResponseCommand.class, this);
        super.onDestroy();
    }

    private void initSendButton(View view) {
        this.sendButton = view.findViewById(R.id.send_character_message_button);

        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString();
            state.setSfx(sfxSelected ? sfxName : "");
            client.sendICMessage(state, message);
            messageInput.setText("");
        });
    }

    private void initListOfCharacterButtons(View view) {
        this.listOfCharacterButtons = view.findViewById(R.id.list_of_character_buttons);
        buttonsAdapter = new SpriteButtonsAdapter(currentIniFile == null ? null : currentIniFile.getButtons(), design, state);
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

        this.backgroundAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                backgroundNames == null ? new ArrayList<>() : new ArrayList<>(Arrays.asList(backgroundNames)));

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
        FileArrayAdapter adapter = new FileArrayAdapter(getContext(), android.R.layout.simple_spinner_item,
                characterData == null ? new File[0] : characterData.getIniFiles());
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
                //TODO: add placeholders for left right and center
                .load(design.getPositionToFileMap().get(state.getPosition()))
                .into(positionButton);
        this.positionButton.setOnClickListener(v -> {
            SpritePosition newPosition = state.getPosition().nextPosition();
            state.setPosition(newPosition);
            Glide.with(SceneFragment.this)
                    .load(design.getPositionToFileMap().get(newPosition))
                    .into(positionButton);
        });
    }

    private void initFlipButton(View view) {

        this.flipButton = view.findViewById(R.id.flip_button);
        Glide.with(this)
                // TODO: add placeholder
                .load(design.getFlipToFileMap().get(state.getFlip()))
                .into(flipButton);
        this.flipButton.setOnClickListener(v -> {
            SpriteFlip newFlip = state.getFlip().nextFlip();
            state.setFlip(newFlip);
            Glide.with(SceneFragment.this)
                    .load(design.getFlipToFileMap().get(newFlip))
                    .into(flipButton);
        });
    }

    private void initSfxButton(View view) {
        this.sfxButton = view.findViewById(R.id.sfx_button);
        Glide.with(this)
                // TODO: add placeholder
                .load(design.getSfxButtonsFiles()[sfxSelected ? 1 : 0])
                .into(sfxButton);
        this.sfxButton.setOnClickListener(v -> {
            sfxSelected = !sfxSelected;
            Glide.with(SceneFragment.this)
                    .load(design.getSfxButtonsFiles()[sfxSelected ? 1 : 0])
                    .into(sfxButton);
        });

    }


}