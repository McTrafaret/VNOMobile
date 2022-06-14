package com.example.vnolib.connection;


import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.CommandParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketThread {

    private static final String SERVER_ENCODING = "cp1251";

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

    public void join() throws InterruptedException {
        reader.join();
        writer.join();
    }

    private static class ReaderThread extends Thread {

        private static final int BUF_SIZE = 4096;

        private final ServerConnection connection;

        public ReaderThread(ServerConnection connection) {
            this.connection = connection;
        }

        private static void moveToStart(byte[] content, int offset, int size) {
            int i = 0;
            int stopOffset = offset + size;
            for (int j = offset; j < stopOffset; j++) {
                content[i] = content[j];
                i++;
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[BUF_SIZE];
            int filled = 0;
            try (InputStream inputStream = connection.getSocket().getInputStream()) {
                while (connection.getStatus().equals(ConnectionStatus.CONNECTED)) {
                    try {
                        filled += inputStream.read(buffer, filled, BUF_SIZE - filled);
                    } catch (IOException ex) {
                        log.warn("While reading from socket: ", ex);
                        connection.setStatus(ConnectionStatus.DISCONNECTED);
                        break;
                    }
                    int i = 0;
                    while (filled > 0 && i < filled) {
                        if (buffer[i] == '%') {
                            try {
                                String commandString = new String(buffer, 0, i + 1, SERVER_ENCODING);
                                BaseCommand command = CommandParser.parse(commandString);
                                log.debug(command.toString());
                                connection.getCommandsToRead().put(command);
                            } catch (Exception ex) {
                                log.error("While parsing command: ", ex);
                            }
                            filled -= i + 1;
                            moveToStart(buffer, i + 1, filled);
                            i = 0;
                        }
                        i++;
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
            try (OutputStream out = connection.getSocket().getOutputStream()) {
                while (connection.getStatus().equals(ConnectionStatus.CONNECTED)) {
                    try {
                        BaseCommand command = connection.getCommandsToSend().take();
                        try {
                            out.write(command.toVnoString().getBytes(SERVER_ENCODING));
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
