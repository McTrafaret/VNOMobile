package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.Getter;
import lombok.ToString;

/**
 * This command is sent by server to all clients when someone appears in location
 * without leaving from another. Typically on new connections.
 */
@ToString
@Getter
@Command(name = "RaC", numOfArguments = 2)
public class RaCCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int idOfTheLocation;

    @CommandArgument(index = 1, optional = false)
    int newNumberOfCharacters;

    @Override
    public void handle(Client client) {
        client.changeAreaPopulation(idOfTheLocation, newNumberOfCharacters);
    }
}
