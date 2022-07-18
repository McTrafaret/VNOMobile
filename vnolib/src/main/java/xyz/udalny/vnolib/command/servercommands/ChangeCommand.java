package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;

import lombok.ToString;

/**
 * This command is sent by the client to server if the client already has some character and it wishes to change it.
 * The behaviour of server in this case is to reset the client's character to none and make the character pickable again.
 */
@ToString
@Command(name = "Change")
public class ChangeCommand extends BaseCommand {
}
