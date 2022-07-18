package xyz.udalny.vnolib.command.ascommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by master if authentication failed
 */
@ToString
@Command(name = "No")
public class NoCommand extends BaseCommand {
}
