package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;

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
