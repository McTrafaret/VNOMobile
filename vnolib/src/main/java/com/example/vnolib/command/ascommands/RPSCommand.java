package com.example.vnolib.command.ascommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by client to master to request the server with specific index
 */
@ToString
@Command(name = "RPS", numOfArguments = 1)
public class RPSCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int serverIndex;

    public RPSCommand(int serverIndex) {
        this.serverIndex = serverIndex;
    }
}
