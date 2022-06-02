package com.example.vnomobile.command.servercommands;

import com.example.vnomobile.command.BaseCommand;
import com.example.vnomobile.command.Command;
import com.example.vnomobile.command.CommandArgument;
import com.example.vnomobile.command.CommandType;

@Command(name = "CT", numOfArguments = 2)
public class CTCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    private String username;

    @CommandArgument(index = 1)
    private String message;

    public CTCommand() {
        super(CommandType.CT.toString());
    }

    public CTCommand(String name, String username, String message) {
        super(name);
        this.username = username;
        this.message = message;
    }
}
