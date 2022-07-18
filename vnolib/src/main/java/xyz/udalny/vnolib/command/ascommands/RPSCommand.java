package xyz.udalny.vnolib.command.ascommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by client to master to request the server with specific index
 */
@ToString
@Command(name = "RPS", numOfArguments = 1)
public class RPSCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int serverIndex;

    public RPSCommand(int serverIndex) {
        this.serverIndex = serverIndex;
    }
}
