package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command requests information about moderator with specific ID.
 *
 * ID is in fact the line number in moderators.txt or something like that
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Command(name = "GmB", numOfArguments = 1)
public class GmBRequestCommand {

    @CommandArgument(index = 0, optional = false)
    int modId;
}
