package xyz.udalny.vnolib.connection;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.LinkedBlockingQueue;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.ascommands.COCommand;
import xyz.udalny.vnolib.command.ascommands.RPSCommand;
import xyz.udalny.vnolib.command.ascommands.VERCommand;

public class ASConnection extends ServerConnection {

    public ASConnection(String host, Integer port, CommandHandler handler) {
        super(host, port, handler);
    }

    public ASConnection(String host, Integer port, LinkedBlockingQueue<BaseCommand> commandsToReadReference, CommandHandler handler) {
        super(host, port, commandsToReadReference, handler);
    }

    public void sendLoginRequest(String login, String password) throws NoSuchAlgorithmException {
        sendCommand(new VERCommand());
        sendCommand(new COCommand(login, password));
    }

    public void sendServerRequest(int index) {
        sendCommand(new RPSCommand(index));
    }
}
