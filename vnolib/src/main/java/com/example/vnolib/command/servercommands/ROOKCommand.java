package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;


/**
 * This command is sent in response to {@link ARCCommand} after successful area change.
 */
@ToString
@Command(name = "ROOK", numOfArguments = 2)
public class ROOKCommand extends BaseCommand {

    /**
     * was 30 in my packets
     */
    @CommandArgument(index = 0, optional = false)
    String arg1;

    /**
     * was 30 in my packets
     */
    @CommandArgument(index = 1, optional = false)
    String arg2;
}
