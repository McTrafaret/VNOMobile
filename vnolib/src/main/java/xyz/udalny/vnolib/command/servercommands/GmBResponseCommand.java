package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent in response to {@link GmBRequestCommand} It gives information about moderator with specific ID.
 *
 * ID is in fact the line number in moderators.txt or something like that
 */
@ToString
@Command(name = "GmB", numOfArguments = 2)
public class GmBResponseCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int modId;

    @CommandArgument(index = 1, optional = false)
    String modUsername;
}
