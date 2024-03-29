package xyz.udalny.vnolib;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.model.CharacterState;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyClass {

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.startCommandHandler();
            client.connectToMaster();
            Thread.sleep(1000);
            client.requestServers();
            Thread.sleep(1000);
            client.connectToServer(client.getServers().get(3));
            client.disconnectFromMaster();
            Thread.sleep(1000);
            client.requestCharacters();
            client.requestAreas();
            Thread.sleep(2000);
            CharacterState state = new CharacterState();
            state.setSpriteName("1");
            state.setBackgroundName("hype");
            client.pickCharacter(client.getCharacterByIndex(0), "");
            Thread.sleep(1000);
            client.sendICMessage(state, "hype");
            Thread.sleep(1000);
            log.info("hype");
        } catch (Exception ex) {
            log.error("main: ", ex);
        }
        log.info("exiting main");
    }
}