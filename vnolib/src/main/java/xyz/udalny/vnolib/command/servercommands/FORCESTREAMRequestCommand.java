package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * This command is sent by client to play some music from link.
 * The server will acknowledge it only if the user is moderator or animator.
 */
@ToString
@AllArgsConstructor
@Command(name = "FORCESTREAM", numOfArguments = 1)
public class FORCESTREAMRequestCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String streamLink;
}
