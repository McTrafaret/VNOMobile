package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Command(name = "FORCESTREAM", numOfArguments = 1)
public class FORCESTREAMRequestCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String streamLink;
}
