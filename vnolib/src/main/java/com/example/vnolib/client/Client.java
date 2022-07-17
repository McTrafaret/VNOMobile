package com.example.vnolib.client;

import com.example.vnolib.client.model.Area;
import com.example.vnolib.client.model.BoxName;
import com.example.vnolib.client.model.Character;
import com.example.vnolib.client.model.CharacterState;
import com.example.vnolib.client.model.Item;
import com.example.vnolib.client.model.Server;
import com.example.vnolib.client.model.Track;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.servercommands.enums.LoopingStatus;
import com.example.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnolib.connection.ASConnection;
import com.example.vnolib.connection.ConnectionStatus;
import com.example.vnolib.connection.PublisherCommandHandler;
import com.example.vnolib.connection.VNOConnection;
import com.example.vnolib.exception.ConnectionException;
import com.example.vnolib.exception.NoSuchCharacterException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {

    public static final int MASTER_PORT = 6543;
    public static final String MASTER_IP = "52.73.41.179";

    private ASConnection asConnection;
    private VNOConnection vnoConnection;

    private ClientState state;
    private boolean authenticated = false;

    private String username;

    private final List<Server> servers;

    private boolean commandHandlerRunning = false;
    private final PublisherCommandHandler commandHandler;
    private final LinkedBlockingQueue<BaseCommand> commandsToRead;


    private final ReentrantLock areaLock = new ReentrantLock();
    private Area[] areas;

    private final ReentrantLock charactersLock = new ReentrantLock();
    private Character[] characters;

    private final ReentrantLock itemsLock = new ReentrantLock();
    private Item[] items;

    private final ReentrantLock tracksLock = new ReentrantLock();
    private Track[] tracks;

    private int serverPlayerLimit;
    private int serverNumberOfPlayers;

    private Character currentCharacter = null;
    private Area currentArea = null;

    private boolean isMod = false;

    private Area areaChange = null;

    public Client() {
        state = ClientState.LOGIN;
        servers = Collections.synchronizedList(new ArrayList<Server>());
        commandsToRead = new LinkedBlockingQueue<>();
        commandHandler = new PublisherCommandHandler(commandsToRead, this);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public void setMod(boolean status) {
        this.isMod = status;
    }

    public void setAreas(Area[] areas) {
        this.areas = areas;
    }

    public void setCharacters(Character[] characters) {
        this.characters = characters;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    public void setTracks(Track[] tracks) {
        this.tracks = tracks;
    }

    public int getNumOfAreas() {
        return this.areas.length;
    }

    public int getNumOfCharacters() {
        return this.characters.length;
    }

    public int getNumOfItems() {
        return this.items.length;
    }

    public int getNumOfTracks() {
        return this.tracks.length;
    }

    public void setServerPlayerLimit(int serverPlayerLimit) {
        this.serverPlayerLimit = serverPlayerLimit;
    }

    public void setServerNumberOfPlayers(int serverNumberOfPlayers) {
        this.serverNumberOfPlayers = serverNumberOfPlayers;
    }

    public void setCurrentCharacter(Character character) {
        this.currentCharacter = character;
    }

    public Area getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(Area area) {
        currentArea = area;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }

    public void addArea(Area area) {
        synchronized (areaLock) {
            this.areas[area.getLocationId() - 1] = area;
        }
    }

    public Area getAreaById(int id) {
        synchronized (areaLock) {
            return areas[id - 1];
        }
    }

    public Area[] getAreas() {
        synchronized (areaLock) {
            return Arrays.copyOf(areas, areas.length);
        }
    }

    public void changeAreaPopulation(int areaId, int newPopulation) {
        synchronized (areaLock) {
            this.areas[areaId - 1].setPopulation(newPopulation);
        }
    }

    public void addCharacter(Character character) {
        synchronized (charactersLock) {
            this.characters[character.getCharId() - 1] = character;
        }
    }

    public Character getCharacterByIndex(int index) {
        synchronized (charactersLock) {
            return characters[index];
        }
    }

    public Track[] getTracks() {
        synchronized (tracksLock) {
            return Arrays.copyOf(tracks, tracks.length);
        }
    }

    public String getUsername() {
        return username;
    }

    // O(n) I don't give a shit, I'm punk
    public Character getCharacterByName(String name) throws NoSuchCharacterException {
        synchronized (charactersLock) {
            for (Character character : characters) {
                if (character.getCharName().equals(name)) {
                    return character;
                }
            }
            throw new NoSuchCharacterException(String.format("No character with name %s", name));
        }
    }

    public void addItem(Item item) {
        synchronized (itemsLock) {
            this.items[item.getItemId() - 1] = item;
        }
    }

    public void addTrack(Track track) {
        synchronized (tracksLock) {
            this.tracks[track.getTrackId() - 1] = track;
        }
    }

    public void authenticate(String login, String password) throws ConnectionException, NoSuchAlgorithmException {
        if (!asConnection.getStatus().equals(ConnectionStatus.CONNECTED)) {
            // TODO Exception
            throw new ConnectionException("Not connected to master");
        }
        asConnection.sendLoginRequest(login, password);
    }

    public void requestServer(int index) throws ConnectionException {
        if (!asConnection.getStatus().equals(ConnectionStatus.CONNECTED)) {
            // TODO Exception
            throw new ConnectionException("Not connected to master");
        }
        asConnection.sendServerRequest(index);
    }

    public void requestServers() throws ConnectionException {
        requestServer(0);
    }

    public void requestAreas() {
        for (int i = 1; i <= areas.length; i++) {
            vnoConnection.sendAreaRequest(i);
        }
    }

    public void requestCharacters() {
        for (int i = 1; i <= characters.length; i += 2) {
            vnoConnection.sendCharacterRequest(i);
        }
    }

    public void requestTracks() {
        for (int i = 1; i <= tracks.length; i++) {
            vnoConnection.sendTrackRequest(i);
        }
    }

    public void requestItems() {
        // TODO: vpadlu razbiratsya s itemami, ih nikto ne yuzaet
    }

    public void pickCharacter(Character character, String password) {
        if (currentCharacter != null) {
            vnoConnection.sendChangeRequest();
            currentCharacter = null;
        }
        vnoConnection.sendPickCharacterRequest(character.getCharId(), password);
    }

    public void sendICMessage(BoxName boxName,
                              String spriteName,
                              String message,
                              MessageColor color,
                              String backgroundImageName,
                              SpritePosition position,
                              SpriteFlip flip,
                              String sfx) {

        String boxNameString = boxName.equals(BoxName.USERNAME) ? username : boxName.getRequestString();
        vnoConnection.sendICMessage(currentCharacter.getCharName(), spriteName, message, boxNameString, color, currentCharacter.getCharId(), backgroundImageName, position, flip, sfx);
    }

    public void sendICMessage(CharacterState state, String message) {

        BoxName boxName = state.getBoxName();
        String spriteName = state.getSpriteName();
        MessageColor color = state.getMessageColor();
        String backgroundImageName = state.getBackgroundName();
        SpritePosition position = state.getPosition();
        SpriteFlip flip = state.getFlip();
        String sfx = state.getSfx();

        sendICMessage(boxName, spriteName, message, color, backgroundImageName, position, flip, sfx);
    }

    public void sendOOCMessage(String message) {
        vnoConnection.sendOOCMessage(username, message);
    }

    public void playTrack(String trackName, LoopingStatus loopingStatus) {
        Track trackToPlay = null;
        synchronized (tracksLock) {
            for (Track track : tracks) {
                if (track.getTrackName().equals(trackName)) {
                    trackToPlay = track;
                    break;
                }
            }
        }
        if(trackToPlay == null) {
            log.error("No such track {}", trackName);
            return;
        }

        playTrack(trackToPlay, loopingStatus);
    }

    public void playTrack(Track track, LoopingStatus loopingStatus) {
        vnoConnection.sendPlayTrackRequest(currentCharacter.getCharName(), track.getTrackName(), track.getTrackId(), currentCharacter.getCharId(), loopingStatus);
    }

    public void requestAreaChange(Area area) {
        this.areaChange = area;
        vnoConnection.sendChangeAreaRequest(area.getLocationId());
    }

    public void changeArea() {
        this.currentArea = areaChange;
        this.areaChange = null;
    }

    public void addServer(Server server) {
        synchronized (servers) {
            servers.add(server.getIndex(), server);
        }
    }

    public void connectToMaster() throws ConnectionException, IOException {
        if(asConnection == null || asConnection.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            asConnection = new ASConnection(MASTER_IP, MASTER_PORT, commandsToRead, commandHandler);
            asConnection.connect();
            return;
        }
        throw new ConnectionException(String.format("Already connected to master. Ip: %s", asConnection.getHost()));
    }

    public void disconnectFromMaster() throws IOException, ConnectionException {
        if (asConnection == null || asConnection.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            throw new ConnectionException("Not connected to master");
        }
        asConnection.disconnect();
        asConnection = null;
    }

    public void connectToServer(Server server) throws ConnectionException, IOException {
        if(vnoConnection == null || vnoConnection.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            vnoConnection = new VNOConnection(server, commandsToRead, commandHandler);
            vnoConnection.connect();
            return;
        }
        throw new ConnectionException(String.format("Already connected to server. Server info: %s", vnoConnection.getServer()));
    }

    public void disconnectFromServer() throws ConnectionException, IOException {
        if (vnoConnection == null || vnoConnection.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            throw new ConnectionException("Not connected to master");
        }
        vnoConnection.disconnect();
        vnoConnection = null;
    }

    public void startCommandHandler() {
        commandHandler.start();
    }

    public void stopCommandHandler() {
        commandHandler.stopHandler();
    }

    public void subscribeToCommand(Class<? extends BaseCommand> commandClass, Object object) {
        commandHandler.subscribeToCommand(commandClass, object);
    }

    public void unsubscribeFromCommand(Class<? extends BaseCommand> commandClass, Object object) {
        commandHandler.unsubscribeFromCommand(commandClass, object);
    }

    public void unsubscribeAll() {
        commandHandler.unsubscribeAll();
    }

    public void getMod(String modPassword) throws ConnectionException {
        if (vnoConnection == null || vnoConnection.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            throw new ConnectionException("Not connected to server");
        }
        vnoConnection.requestMod(modPassword);
    }
}
