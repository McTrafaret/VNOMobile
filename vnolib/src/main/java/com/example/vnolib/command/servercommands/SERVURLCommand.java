package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

@ToString
@Command(name = "SERVURL", numOfArguments = 1)
public class SERVURLCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String url;
}
