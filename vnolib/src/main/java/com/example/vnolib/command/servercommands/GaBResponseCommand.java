package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

@ToString
@Command(name = "GaB", numOfArguments = 2)
public class GaBResponseCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int animatorId;

    @CommandArgument(index = 1, optional = false)
    String animatorUsername;

}
