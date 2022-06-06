package com.example.vnolib.connection;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.ascommands.COCommand;
import com.example.vnolib.command.ascommands.RPSCommand;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.LinkedBlockingQueue;

public class ASConnection extends ServerConnection {

    public ASConnection(String host, Integer port) {
        super(host, port);
    }

    public ASConnection(String host, Integer port, LinkedBlockingQueue<BaseCommand> commandsToReadReference) {
        super(host, port, commandsToReadReference);
    }

    public void sendServersRequest() {
        //TODO
    }

    public void sendLoginRequest(String login, String password) throws NoSuchAlgorithmException, InterruptedException {
        commandsToSend.put(new COCommand(login, password));
    }

    public void sendServerRequest(int index) throws InterruptedException {
        commandsToSend.put(new RPSCommand(index));
    }
}
