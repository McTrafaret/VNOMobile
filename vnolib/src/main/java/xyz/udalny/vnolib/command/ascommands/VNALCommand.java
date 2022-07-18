package xyz.udalny.vnolib.command.ascommands;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.ClientState;
import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is sent by master after successful authentication
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "VNAL", numOfArguments = 1)
public class VNALCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String username;

    @Override
    public void handle(Client client) {
        client.setUsername(username);
        client.setState(ClientState.CHOOSING_SERVER);
    }
}
