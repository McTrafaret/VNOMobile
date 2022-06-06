package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

@ToString
@Command(name = "RMD", numOfArguments = 2)
public class RMDCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String trackName;

    @CommandArgument(index = 1, optional = false)
    String trackLocation;
}
