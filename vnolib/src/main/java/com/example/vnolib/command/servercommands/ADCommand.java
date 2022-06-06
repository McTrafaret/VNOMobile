package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

@ToString
@Command(name="AD", numOfArguments = 4)
public class ADCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int locationId;

    @CommandArgument(index = 1, optional = false)
    String locationName;

    @CommandArgument(index = 2, optional = false)
    int locationPopulation;

    @CommandArgument(index = 3, optional = false)
    String backgroundNamePattern;

    @CommandArgument(index = 4)
    String arg5;

}
