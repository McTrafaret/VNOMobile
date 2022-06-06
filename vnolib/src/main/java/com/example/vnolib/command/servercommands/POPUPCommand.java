package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;


/**
 * This command is sent by server in response to {@link LISTCommand}.
 *
 * Represents ONE character in location.
 */
@ToString
@Command(name = "POPUP", numOfArguments = 1)
public class POPUPCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String popupInfo;
}
