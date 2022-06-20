package com.example.vnolib.connection;

import com.example.vnolib.client.model.Server;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.servercommands.ARCCommand;
import com.example.vnolib.command.servercommands.ChangeCommand;
import com.example.vnolib.command.servercommands.MCCommand;
import com.example.vnolib.command.servercommands.MODCommand;
import com.example.vnolib.command.servercommands.MSCommand;
import com.example.vnolib.command.servercommands.RADCommand;
import com.example.vnolib.command.servercommands.RCDCommand;
import com.example.vnolib.command.servercommands.RMDCommand;
import com.example.vnolib.command.servercommands.ReqCommand;
import com.example.vnolib.command.servercommands.enums.LoopingStatus;
import com.example.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;

import java.util.concurrent.LinkedBlockingQueue;

public class VNOConnection extends ServerConnection {

    private final Server server;

    public VNOConnection(Server server, CommandHandler handler) {
        super(server.getIp(), server.getPort(), handler);
        this.server = server;
    }

    public VNOConnection(Server server, LinkedBlockingQueue<BaseCommand> commandsToReadReference, CommandHandler handler) {
        super(server.getIp(), server.getPort(), commandsToReadReference, handler);
        this.server = server;
    }

    public VNOConnection(String host, Integer port, CommandHandler handler) {
        super(host, port, handler);
        this.server = Server.builder()
                .ip(host)
                .port(port)
                .build();
    }

    public VNOConnection(String host, Integer port, LinkedBlockingQueue<BaseCommand> commandsToReadReference, CommandHandler handler) {
        super(host, port, commandsToReadReference, handler);
        this.server = Server.builder()
                .ip(host)
                .port(port)
                .build();
    }

    public Server getServer() {
        return server;
    }

    public void requestMod(String modPassword) {
        sendCommand(new MODCommand(modPassword));
    }

    public void sendAreaRequest(int areaId) {
        sendCommand(new RADCommand(areaId));
    }

    public void sendCharacterRequest(int characterId) {
        sendCommand(new RCDCommand(characterId));
    }

    public void sendTrackRequest(int trackId) {
        sendCommand(new RMDCommand(trackId));
    }

    public void sendPickCharacterRequest(int charId, String password) {
        sendCommand(new ReqCommand(charId, password));
    }

    public void sendChangeRequest() {
        sendCommand(new ChangeCommand());
    }

    public void sendICMessage(String charName, String spriteName, String message, String boxNameString, MessageColor color, int charId, String backgroundImageName, SpritePosition position, SpriteFlip flip, String sfx) {
        sendCommand(MSCommand.builder()
                .characterName(charName)
                .spriteName(spriteName)
                .message(message)
                .boxName(boxNameString)
                .messageColor(color)
                .characterId(charId)
                .backgroundImageName(backgroundImageName)
                .position(position)
                .flip(flip)
                .sfx(sfx)
                .build());
    }

    public void sendPlayTrackRequest(String charName, String trackName, int trackId, int charId, LoopingStatus loopingStatus) {
        sendCommand(MCCommand.builder()
                .characterName(charName)
                .trackName(trackName)
                .trackId(trackId)
                .characterId(charId)
                .loopingStatus(loopingStatus)
                .build());
    }

    public void sendChangeAreaRequest(int locationId) {
        sendCommand(new ARCCommand(locationId, ""));
    }
}
