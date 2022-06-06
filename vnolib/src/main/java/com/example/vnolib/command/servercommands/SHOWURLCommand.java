package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * This command is sent by client to server to show some picture from url in all clients.
 *
 * Works only if user is a moderator or animator.
 */
@AllArgsConstructor
@ToString
@Command(name = "SHOWURL", numOfArguments = 1)
public class SHOWURLCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String url;
}
