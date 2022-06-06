package com.example.vnolib.command.servercommands;

import com.example.vnolib.client.Client;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by server in response to {@link MODCommand} if moderator or animator rights are allowed.
 */
@ToString
@Command(name = "MODOK")
public class MODOKCommand extends BaseCommand {

    @Override
    public void handle(Client client) {
        client.setMod(true);
    }
}
