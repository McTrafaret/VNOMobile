package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by server to show some picture from url in client.
 *
 * Works in client only if the url is in whitelist
 */
@ToString
@Command(name = "SERVURL", numOfArguments = 1)
public class SERVURLCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String url;
}
