package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is sent to request the location with specific id
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "RAD", numOfArguments = 1)
public class RADCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int locationId;
}
