package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by server to play some music from link.
 *
 * The client refuses to play anything from the url if it is not listed in whitelist
 */
@ToString
@Command(name = "FORCESTREAM", numOfArguments = 2)
public class FORCESTREAMResponseCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String streamLink;

    @CommandArgument(index = 1, optional = false)
    String characterName;

}
