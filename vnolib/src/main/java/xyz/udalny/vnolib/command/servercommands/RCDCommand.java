package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is sent to request character with specific ID
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "RCD", numOfArguments = 1)
public class RCDCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int charId;
}
