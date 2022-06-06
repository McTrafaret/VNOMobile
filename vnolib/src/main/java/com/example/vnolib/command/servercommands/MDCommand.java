package com.example.vnolib.command.servercommands;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.model.Track;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by in response to {@link RMDCommand}. Gives information about some track.
 */
@ToString
@Command(name = "MD", numOfArguments = 3)
public class MDCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int trackId;

    @CommandArgument(index = 1, optional = false)
    String trackName;

    @CommandArgument(index = 2, optional = false)
    String trackLocation;

    @Override
    public void handle(Client client) {
        client.addTrack(new Track(trackId, trackName, trackLocation));
    }
}
