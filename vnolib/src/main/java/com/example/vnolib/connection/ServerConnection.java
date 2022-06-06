package com.example.vnolib.connection;

import com.example.vnolib.command.BaseCommand;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public abstract class ServerConnection {

    protected final LinkedBlockingQueue<BaseCommand> commandsToSend;
    protected final LinkedBlockingQueue<BaseCommand> commandsToRead;

    protected Socket socket;
    protected ConnectionStatus status;
    protected SocketThread socketThread;

    protected final String host;
    protected final Integer port;

    public ServerConnection(String host, Integer port) {
        this.host = host;
        this.port = port;
        this.status = ConnectionStatus.DISCONNECTED;
        this.commandsToSend = new LinkedBlockingQueue<>();
        this.commandsToRead = new LinkedBlockingQueue<>();
    }

    public ServerConnection(String host, Integer port, LinkedBlockingQueue<BaseCommand> commandsToReadReference) {
        this.host = host;
        this.port = port;
        this.status = ConnectionStatus.DISCONNECTED;
        this.commandsToSend = new LinkedBlockingQueue<>();
        this.commandsToRead = commandsToReadReference;
    }

    public synchronized void setStatus(ConnectionStatus status) {
        this.status = status;
    }

    public synchronized ConnectionStatus getStatus() {
        return status;
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        status = ConnectionStatus.CONNECTED;

        socketThread = new SocketThread(this);
        socketThread.start();
    }

    public void disconnect() throws IOException {
        socket.close();
        status = ConnectionStatus.DISCONNECTED;
        while(true) {
            try {
                socketThread._wait();
                break;
            } catch (InterruptedException e) {
                log.warn("Interrupted while stopping the threads");
            }
        }
    }
}
