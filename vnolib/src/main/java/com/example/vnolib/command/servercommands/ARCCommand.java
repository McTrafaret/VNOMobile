package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "ARC", numOfArguments = 2)
public class ARCCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int areadId;

    @CommandArgument(index = 1)
    String arg2;
}
