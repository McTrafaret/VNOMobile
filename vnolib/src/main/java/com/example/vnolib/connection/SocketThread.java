package com.example.vnolib.connection;


import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.CommandParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketThread {

    private final ServerConnection connection;

    private final Thread reader;
    private final Thread writer;

    public SocketThread(ServerConnection connection) {
        this.connection = connection;
        this.reader = new ReaderThread(this.connection);
        this.writer = new WriterThread(this.connection);
    }

    public void start() {
        reader.start();
        writer.start();
    }

    public void _wait() throws InterruptedException {
        reader.join();
        writer.join();
    }

    private static class ReaderThread extends Thread {

        private final ServerConnection connection;

        public ReaderThread(ServerConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try (InputStream inputStream = connection.getSocket().getInputStream()) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int bytesFilled = 0;
                while (connection.getStatus().equals(ConnectionStatus.CONNECTED)) {
                    int readReturnValue;
                    try {
                        readReturnValue = bufferedReader.read();
                    } catch (SocketException ex) {
                        log.warn("While reading from socket: ", ex);
                        connection.setStatus(ConnectionStatus.DISCONNECTED);
                        break;
                    }
                    if (readReturnValue == -1) {
                        break;
                    }
                    byteBuffer.put((byte) readReturnValue);
                    bytesFilled += 1;
                    if (readReturnValue == '%') {
                        try {
                            byte[] commandBytes = Arrays.copyOfRange(byteBuffer.array(), 0, bytesFilled);
                            BaseCommand command = CommandParser.parse(new String(commandBytes));
                            log.debug(command.toString());
                            connection.getCommandsToRead().put(command);
                        } catch (Exception ex) {
                            log.error("run: ", ex);
                        }
                        byteBuffer.clear();
                        bytesFilled = 0;
                    }
                }
            } catch (Exception ex) {
                log.error("run: ", ex);
            }
        }
    }

    private static class WriterThread extends Thread {

        private final ServerConnection connection;

        public WriterThread(ServerConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try(OutputStream out = connection.getSocket().getOutputStream()) {
                while (connection.getStatus().equals(ConnectionStatus.CONNECTED)) {
                    try {
                        BaseCommand command = connection.getCommandsToSend().take();
                        try {
                            out.write(command.toVnoString().getBytes());
                        } catch (SocketException ex) {
                            log.warn("While reading from socket: ", ex);
                            connection.setStatus(ConnectionStatus.DISCONNECTED);
                            break;
                        }
                    } catch (Exception ex) {
                        log.error("run: ", ex);
                    }
                }
            } catch (Exception ex) {
                log.error("run: ", ex);
            }
        }
    }
}
