package xyz.udalny.vnolib.command.ascommands;

import lombok.ToString;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

/**
 * This command needs to be sent or it won't authenticate
 */
@ToString
@Command(name = "VER", numOfArguments = 2)
public class VERCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String arg1;

    @CommandArgument(index = 1, optional = false)
    String versionNumber;

    public VERCommand() {
        this.arg1 = "C";
        this.versionNumber = "2.3";
    }
}
