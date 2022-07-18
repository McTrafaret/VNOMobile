package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command requests moderator or animator rights.
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MODCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    private static final String argument1 = "AUTH";

    @CommandArgument(index = 1, optional = false)
    private String password;
}
