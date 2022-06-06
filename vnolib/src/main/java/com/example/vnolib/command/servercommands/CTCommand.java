package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is used to send OOC messages. Used both by client and server.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Command(name = "CT", numOfArguments = 2)
public class CTCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    private String username;

    @CommandArgument(index = 1)
    private String message;
}
