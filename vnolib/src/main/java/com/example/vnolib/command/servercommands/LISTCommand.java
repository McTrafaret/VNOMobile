package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * This command requests characters in location with specific id.
 */
@ToString
@AllArgsConstructor
@Command(name = "LIST", numOfArguments = 1)
public class LISTCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int locationId;
}
