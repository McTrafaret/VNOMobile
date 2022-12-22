package xyz.udalny.vnolib.command.servercommands;

import lombok.Getter;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by server to show some picture from url in client.
 *
 * Works in client only if the url is in whitelist
 */
@ToString
@Command(name = "SERVURL", numOfArguments = 1)
@Getter
public class SERVURLCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String url;
}
