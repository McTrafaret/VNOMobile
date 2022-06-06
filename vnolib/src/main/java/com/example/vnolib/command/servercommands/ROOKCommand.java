package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;


@ToString
@Command(name = "ROOK", numOfArguments = 2)
public class ROOKCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String arg1;

    @CommandArgument(index = 1, optional = false)
    String arg2;
}
