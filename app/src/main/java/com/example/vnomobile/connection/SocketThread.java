package com.example.vnomobile.connection;

import com.example.vnomobile.command.BaseCommand;
import com.example.vnomobile.command.CommandParser;

import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketThread {

    private LinkedBlockingQueue<BaseCommand> receivedCommands;
    private LinkedBlockingQueue<BaseCommand> commandsToSend;
    private Socket socket;

    public SocketThread(LinkedBlockingQueue<BaseCommand> receivedCommands, LinkedBlockingQueue<BaseCommand> commandsToSend, Socket socket) {
        this.receivedCommands = receivedCommands;
        this.commandsToSend = commandsToSend;
        this.socket = socket;
    }

    public void run() {
    }

    public void reader() {
        InputStream inputStream = socket.getInputStream();
        ByteBuffer byteBuffer = new ByteBuffer();
        while(true) {
            int readReturnValue = inputStream.read();
            if(readReturnValue == -1) {
                // TODO: handle end of stream meaning the connection may be closed
            }
            byteBuffer.put((byte) readReturnValue);
            if(readReturnValue == '%') {
                receivedCommands.put(CommandParser.parse(byteBuffer.toString()));
            }
        }
    }
}
