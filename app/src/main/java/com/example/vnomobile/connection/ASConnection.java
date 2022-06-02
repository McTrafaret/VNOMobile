package com.example.vnomobile.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ASConnection {

    private ConnectionStatus status;
    private Socket socket;

    private final String ip;
    private final Short port;

    public ASConnection(String ip, Short port) {
        this.ip = ip;
        this.port = port;
        this.status = ConnectionStatus.DISCONNECTED;
    }

    public void connect() throws ConnectException, IOException, UnknownHostException {
        if(this.status.equals(ConnectionStatus.CONNECTED)) {
            throw new ConnectException("Already connected to AS");
        }

        this.socket = new Socket(ip, port);
        this.status = ConnectionStatus.CONNECTED;
    }

    public void disconnect() throws IOException {
        this.socket.close();
        this.status = ConnectionStatus.DISCONNECTED;
    }

    public void sendServersRequest() {
       //TODO
    }

    public void sendLoginRequest(String login, String password) {
        //TODO
    }
}
