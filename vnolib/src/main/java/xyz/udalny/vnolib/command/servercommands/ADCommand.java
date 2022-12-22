package xyz.udalny.vnolib.command.servercommands;

import lombok.Setter;
import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.model.Area;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.ToString;

/**
 * This command is sent by the server in response to {@link RADCommand}. It gives information about location(area).
 */
@ToString
@Setter
@Command(name="AD", numOfArguments = 4)
public class ADCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int locationId;

    @CommandArgument(index = 1, optional = false)
    String locationName;

    @CommandArgument(index = 2, optional = false)
    int locationPopulation;

    @CommandArgument(index = 3, optional = false)
    String backgroundNamePattern;

    @CommandArgument(index = 4)
    String arg5;

    @Override
    public void handle(Client client) {
        client.addArea(new Area(locationId, locationName, locationPopulation, backgroundNamePattern, arg5));
    }
}
