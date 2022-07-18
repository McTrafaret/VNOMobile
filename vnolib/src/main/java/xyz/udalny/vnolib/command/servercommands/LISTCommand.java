package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.ToString;


/**
 * This command requests characters in location with specific id.
 */
@ToString
@AllArgsConstructor
@Command(name = "LIST", numOfArguments = 1)
public class LISTCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int locationId;
}
