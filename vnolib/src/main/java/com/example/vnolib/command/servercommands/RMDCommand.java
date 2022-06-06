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
@Command(name = "RMD", numOfArguments = 1)
public class RMDCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int trackId;
}
