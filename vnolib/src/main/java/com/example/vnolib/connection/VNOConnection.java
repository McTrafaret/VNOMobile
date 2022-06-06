package com.example.vnolib.connection;

import com.example.vnolib.client.model.Server;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.servercommands.MODCommand;

import java.util.concurrent.LinkedBlockingQueue;

public class VNOConnection extends ServerConnection {

    private final Server server;

    public VNOConnection(Server server) {
        super(server.getIp(), server.getPort());
        this.server = server;
    }

    public VNOConnection(Server server, LinkedBlockingQueue<BaseCommand> commandsToReadReference) {
        super(server.getIp(), server.getPort(), commandsToReadReference);
        this.server = server;
    }

    public VNOConnection(String host, Integer port) {
        super(host, port);
        this.server = Server.builder()
                .ip(host)
                .port(port)
                .build();
    }

    public VNOConnection(String host, Integer port, LinkedBlockingQueue<BaseCommand> commandsToReadReference) {
        super(host, port, commandsToReadReference);
        this.server = Server.builder()
                .ip(host)
                .port(port)
                .build();
    }

    public Server getServer() {
        return server;
    }

    public void requestMod(String modPassword) throws InterruptedException {
        commandsToRead.put(new MODCommand(modPassword));
    }
}
