package com.example.vnolib.client;

import com.example.vnolib.client.model.Server;
import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.connection.ASConnection;
import com.example.vnolib.connection.ConnectionStatus;
import com.example.vnolib.connection.VNOConnection;
import com.example.vnolib.exception.ConnectionException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {

    public static final int MASTER_PORT = 6543;
    public static final String MASTER_IP = "52.73.41.179";

    private ASConnection asConnection;
    private VNOConnection vnoConnection;

    private ClientState state;
    private boolean authenticated = false;

    private String username;

    private final List<Server> servers;

    private boolean commandHandlerRunning = false;
    private final CommandHandler commandHandler;
    private final LinkedBlockingQueue<BaseCommand> commandsToRead;

    private boolean isMod = false;

    public Client() {
        state = ClientState.LOGIN;
        servers = Collections.synchronizedList(new ArrayList<Server>());
        commandsToRead = new LinkedBlockingQueue<>();
        commandHandler = new CommandHandler(this);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public void setMod(boolean status) {

    }

    public void authenticate(String login, String password) throws ConnectionException, NoSuchAlgorithmException, InterruptedException {
        if(!asConnection.getStatus().equals(ConnectionStatus.CONNECTED)) {
            // TODO Exception
            throw new ConnectionException("Not connected to master");
        }
        asConnection.sendLoginRequest(login, password);
    }

    public void requestServer(int index) throws ConnectionException, InterruptedException {
        if(!asConnection.getStatus().equals(ConnectionStatus.CONNECTED)) {
            // TODO Exception
            throw new ConnectionException("Not connected to master");
        }
        asConnection.sendServerRequest(index);
    }

    public void requestServers() throws InterruptedException, ConnectionException {
        requestServer(0);
    }

    public void addServer(Server server) {
        synchronized (servers) {
            servers.add(server.getIndex(), server);
        }
    }

    public void connectToMaster() throws ConnectionException, IOException {
        if(asConnection != null) {
            throw new ConnectionException(String.format("Already connected to master. Ip: %s", asConnection.getHost()));
        }
        asConnection = new ASConnection(MASTER_IP, MASTER_PORT, commandsToRead);
        asConnection.connect();
    }

    public void connectToServer(Server server) throws ConnectionException, IOException {
        if(vnoConnection != null) {
            throw new ConnectionException(String.format("Already connected to server. Server info: %s", vnoConnection.getServer()));
        }
        vnoConnection = new VNOConnection(server, commandsToRead);
        vnoConnection.connect();
    }

    public void startCommandHandler() {
        commandHandlerRunning = true;
        commandHandler.start();
    }

    private static class CommandHandler extends Thread {

        private final Client client;

        public CommandHandler(Client client) {
            this.client = client;
        }

        @Override
        public void run() {
            while(client.commandHandlerRunning) {
                try {
                    client.commandsToRead.take().handle(client);
                } catch (InterruptedException ex) {
                    log.warn("Interrupted while taking the command to handle");
                }
            }
        }
    }

    public void getMod(String modPassword) throws ConnectionException, InterruptedException {
        if(vnoConnection == null || vnoConnection.getStatus().equals(ConnectionStatus.DISCONNECTED)) {
            throw new ConnectionException("Not connected to server");
        }
        vnoConnection.requestMod(modPassword);
    }
}
