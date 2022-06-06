package com.example.vnolib;

import com.example.vnolib.client.Client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyClass {

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.startCommandHandler();
            client.connectToMaster();
            client.authenticate("Udalny", "likliklik");
            client.requestServers();
            Thread.sleep(4000);
            log.debug("Servers: {}", client.getServers());
            client.connectToServer(client.getServers().get(1));
            Thread.sleep(400000);
        } catch (Exception ex) {
            log.error("main: ", ex);
        }
    }
}