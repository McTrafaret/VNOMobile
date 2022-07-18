package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * This command is sent by the server in response to {@link ReqCommand} if the character is in fact changed.
 */
@ToString
@Slf4j
@Command(name = "Allowed", numOfArguments = 1)
public class AllowedCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String characterName;

    @Override
    public void handle(Client client) {
        try {
            client.setCurrentCharacter(client.getCharacterByName(characterName));
            if(client.getCurrentArea() == null) {
                client.setCurrentArea(client.getAreaById(1));
            }
        } catch (Exception ex) {
            log.error("Failed to set character: ", ex);
        }
    }
}
