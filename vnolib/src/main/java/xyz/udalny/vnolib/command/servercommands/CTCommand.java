package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is used to send OOC messages. Used both by client and server.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Command(name = "CT", numOfArguments = 2)
public class CTCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    private String username;

    @CommandArgument(index = 1)
    private String message;
}
