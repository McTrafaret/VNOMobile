package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent in response to {@link GaBRequestCommand} It gives information about animator with specific ID.
 *
 * ID is in fact the line number in animators.txt or something like that
 */
@ToString
@Command(name = "GaB", numOfArguments = 2)
public class GaBResponseCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int animatorId;

    @CommandArgument(index = 1, optional = false)
    String animatorUsername;

}
