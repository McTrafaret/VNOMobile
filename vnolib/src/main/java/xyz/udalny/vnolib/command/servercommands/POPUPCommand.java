package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;


/**
 * This command is sent by server in response to {@link LISTCommand}.
 *
 * Represents ONE character in location.
 */
@ToString
@Command(name = "POPUP", numOfArguments = 1)
public class POPUPCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String popupInfo;
}
