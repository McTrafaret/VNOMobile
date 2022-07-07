package com.example.vnolib.client;

import com.example.vnolib.command.BaseCommand;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandPublisher {

    private final Map<Class<? extends BaseCommand>, List<Object>> commandToSubscriberMap = new HashMap<>();

    public synchronized void subscribe(Class<? extends BaseCommand> commandClass, Object subscriber) {
        if(!commandToSubscriberMap.containsKey(commandClass)) {
            commandToSubscriberMap.put(commandClass, new ArrayList<>());
        }
        commandToSubscriberMap.get(commandClass).add(subscriber);
    }

    public synchronized void unsubscribe(Class<? extends BaseCommand> commandClass, Object subscriber) {
        if(commandToSubscriberMap.containsKey(commandClass)) {
            commandToSubscriberMap.get(commandClass).remove(subscriber);
        }
    }

    public synchronized void unsubscribeAll() {
        commandToSubscriberMap.clear();
    }

    public void publish(BaseCommand command) throws InvocationTargetException, IllegalAccessException {
        if(commandToSubscriberMap.containsKey(command.getClass())) {
            for (Object subscriber : commandToSubscriberMap.get(command.getClass())) {
                for (Method method : subscriber.getClass().getDeclaredMethods()) {
                    if(method.isAnnotationPresent(OnCommand.class)) {
                        OnCommand onCommand = method.getAnnotation(OnCommand.class);
                        if(onCommand.value().equals(command.getClass())) {
                            method.setAccessible(true);
                            method.invoke(subscriber, command.getClass().cast(command));
                            method.setAccessible(false);
                        }
                    }
                }
            }
        }
    }
}
