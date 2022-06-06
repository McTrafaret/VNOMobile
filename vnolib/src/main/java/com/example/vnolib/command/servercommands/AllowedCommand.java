package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

@ToString
@Command(name = "Allowed", numOfArguments = 1)
public class AllowedCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String characterName;
}
