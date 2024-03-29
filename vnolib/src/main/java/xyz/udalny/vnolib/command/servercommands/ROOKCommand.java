package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;


/**
 * This command is sent in response to {@link ARCCommand} after successful area change.
 */
@ToString
@Command(name = "ROOK", numOfArguments = 2)
public class ROOKCommand extends BaseCommand {

    /**
     * was 30 in my packets
     */
    @CommandArgument(index = 0, optional = false)
    String arg1;

    /**
     * was 30 in my packets
     */
    @CommandArgument(index = 1, optional = false)
    String arg2;

    @Override
    public void handle(Client client) {
        client.changeArea();
    }
}
