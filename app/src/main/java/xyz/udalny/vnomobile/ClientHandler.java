package xyz.udalny.vnomobile;

import xyz.udalny.vnolib.client.Client;

public class ClientHandler {

    private static ClientHandler clientHandler;

    private Client client;

    private ClientHandler() {
        client = new Client();
        client.startCommandHandler();
    }

    public static Client getClient() {
        if(clientHandler == null) {
            clientHandler = new ClientHandler();
        }
        return clientHandler.client;
    }
}
