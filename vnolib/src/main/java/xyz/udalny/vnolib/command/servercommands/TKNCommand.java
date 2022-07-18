package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by server in response to {@link ReqCommand} if character pick is not allowed.
 */
@ToString
@Command(name = "TKN")
public class TKNCommand extends BaseCommand {
}
