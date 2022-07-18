package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command requests information about animator with specific ID.
 *
 * ID is in fact the line number in animators.txt or something like that
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Command(name = "GaB", numOfArguments = 1)
public class GaBRequestCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int animatorId;

}
