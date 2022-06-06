package com.example.vnolib.command.servercommands;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.model.Area;
import com.example.vnolib.client.model.Character;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@Command(name = "CAD", numOfArguments = 6)
public class CADCommand extends BaseCommand {

    @AllArgsConstructor
    @ToString
    public static class CADInfo {
        int charId;
        String charName;
        int taken;
    }

    List<CADInfo> info;

    public CADCommand() {
        this.info = new ArrayList<>();
    }

    public List<CADInfo> getInfo() {
        return info;
    }

    @Override
    public void handle(Client client) {
        for (CADInfo cadInfo : this.info) {
            client.addCharacter(new Character(cadInfo.charId, cadInfo.charName, cadInfo.taken));
        }
    }
}
