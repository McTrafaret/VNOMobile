package com.example.vnolib.command.ascommands;

import com.example.vnolib.client.Client;
import com.example.vnolib.client.model.Server;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * This command is sent by master to client in response to {@link RPSCommand}. It gives information about published servers.
 */
@Slf4j
@ToString
@Command(name = "SDP", numOfArguments = 7)
public class SDPCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int index;

    @CommandArgument(index = 1, optional = false)
    String name;

    @CommandArgument(index = 2, optional = false)
    String ip;

    @CommandArgument(index = 3, optional = false)
    int port;

    @CommandArgument(index = 4)
    String description;

    @CommandArgument(index = 5)
    String link;

    @CommandArgument(index = 6)
    String arg14;

    @Override
    public void handle(Client client) {
        Server server = Server.builder()
                .index(index)
                .name(name)
                .ip(ip)
                .port(port)
                .description(description)
                .link(link)
                .arg14(arg14)
                .build();
        client.addServer(server);
        try {
            client.requestServer(index + 1);
        } catch (Exception ex) {
            log.error("While requesting new server: ", ex);
        }
    }
}
