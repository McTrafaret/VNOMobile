package com.example.vnolib.command.servercommands;


import com.example.vnolib.client.Client;
import com.example.vnolib.client.model.Area;
import com.example.vnolib.client.model.Character;
import com.example.vnolib.client.model.Item;
import com.example.vnolib.client.model.Track;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.Getter;
import lombok.ToString;

/**
 * This command is sent by server after connect. Gives basic information about the server.
 */
@ToString
@Getter
@Command(name = "PC", numOfArguments = 7)
public class PCCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    private int numberOfPlayers;

    @CommandArgument(index = 1, optional = false)
    private int playerLimit;

    @CommandArgument(index = 2, optional = false)
    private int numberOfCharacters;

    @CommandArgument(index = 3, optional = false)
    private int numberOfTracks;

    @CommandArgument(index = 4, optional = false)
    private int numberOfAreas;

    @CommandArgument(index = 5, optional = false)
    private int numberOfItems;

    @CommandArgument(index = 6, optional = false)
    private String argument7;

    @Override
    public void handle(Client client) {
        client.setAreas(new Area[numberOfAreas]);
        client.setCharacters(new Character[numberOfCharacters]);
        client.setItems(new Item[numberOfItems]);
        client.setTracks(new Track[numberOfTracks]);
        client.setServerPlayerLimit(playerLimit);
        client.setServerNumberOfPlayers(numberOfPlayers);
    }
}
