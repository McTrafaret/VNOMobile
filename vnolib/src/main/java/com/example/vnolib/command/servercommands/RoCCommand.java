package com.example.vnolib.command.servercommands;


import com.example.vnolib.client.Client;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by server to all clients if someone leaves one location and goes to another.
 */
@ToString
@Command(name = "RoC", numOfArguments = 4)
public class RoCCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int idOfTheLeaveLocation;

    @CommandArgument(index = 1, optional = false)
    int newNumberOfCharactersInLeaveLocation;

    @CommandArgument(index = 2, optional = false)
    int idOfTheJoinLocation;

    @CommandArgument(index = 3, optional = false)
    int newNumberOfCharactersInJoinLocation;

    @Override
    public void handle(Client client) {
        client.changeAreaPopulation(idOfTheLeaveLocation, newNumberOfCharactersInLeaveLocation);
        client.changeAreaPopulation(idOfTheJoinLocation, newNumberOfCharactersInJoinLocation);
    }
}
