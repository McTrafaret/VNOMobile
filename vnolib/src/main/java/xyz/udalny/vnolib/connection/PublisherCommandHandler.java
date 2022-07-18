package xyz.udalny.vnolib.connection;

import xyz.udalny.vnolib.client.Client;
import xyz.udalny.vnolib.client.CommandPublisher;
import xyz.udalny.vnolib.command.BaseCommand;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.LinkedBlockingQueue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublisherCommandHandler extends Thread implements CommandHandler {

    private final CommandPublisher publisher;
    private boolean running;
    private final LinkedBlockingQueue<BaseCommand> commandsToRead;
    private final Client client;

    public PublisherCommandHandler(LinkedBlockingQueue<BaseCommand> commandsToRead, Client client) {
        super();
        this.commandsToRead = commandsToRead;
        this.publisher = new CommandPublisher();
        this.client = client;
    }

    public void subscribeToCommand(Class<? extends BaseCommand> commandClass, Object object) {
        synchronized (publisher) {
            publisher.subscribe(commandClass, object);
        }
    }

    public void unsubscribeFromCommand(Class<? extends BaseCommand> commandClass, Object object) {
        synchronized (publisher) {
            publisher.unsubscribe(commandClass, object);
        }
    }

    public void unsubscribeAll() {
        publisher.unsubscribeAll();
    }

    public void stopHandler() {
        running = false;
        synchronized (this) {
            notify();
        }
        while(true) {
            try {
                join();
            } catch (InterruptedException e) {
                log.warn("Interrupted while stopping command handler");
            }
            break;
        }
    }

    @Override
    public synchronized void notifyAboutNewCommand() {
        notify();
    }

    @Override
    public synchronized void start() {
        running = true;
        super.start();
    }


    @Override
    public void run() {
        while(running) {
            try {
                synchronized (this) {
                    wait();
                }
                while(true) {
                    BaseCommand command = commandsToRead.poll();
                    if (command == null) {
                        break;
                    }
                    command.handle(client);
                    publisher.publish(command);
                }
            } catch (InterruptedException ex) {
                log.warn("Interrupted while taking the command to handle");
            } catch (InvocationTargetException | IllegalAccessException e) {
                log.error("When publishing command: ", e);
            }
        }
    }

}
