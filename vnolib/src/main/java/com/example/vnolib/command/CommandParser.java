package com.example.vnolib.command;


import com.example.vnolib.exception.CommandException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandParser {

    public static final Pattern COMMAND_PATTERN = Pattern.compile("[^#]+#([^#]*#)*%");

    private static void fillField(Field field, Object object, String value) throws IllegalAccessException {
        field.setAccessible(true);
        if(field.getType().isAssignableFrom(String.class)) {
            value = value
                    .replace("<pound>", "#")
                    .replace("<percent>", "%");
            field.set(object, value);
        }
        else if(field.getType().isAssignableFrom(int.class)) {
            field.set(object, Integer.parseInt(value));
        }
        field.setAccessible(false);
    }

    public static BaseCommand parse(String commandString) throws CommandException {

        commandString = commandString.trim();

        if(!COMMAND_PATTERN.matcher(commandString).matches()) {
            throw new CommandException("Command does not match the pattern");
        }

        String[] nameAndArgs = commandString.split("#");
        String name = nameAndArgs[0];
        String[] args = Arrays.copyOfRange(nameAndArgs, 1, nameAndArgs.length - 1);

        Class<? extends BaseCommand> baseClass = BaseCommand.class;
        Class<? extends BaseCommand> commandClass = CommandType.valueOf(name).commandClass;
        BaseCommand command;
        try {
            Field nameField = baseClass.getDeclaredField("name");

            nameField.setAccessible(true);

            command = commandClass.newInstance();
            nameField.set(command, name);

            nameField.setAccessible(false);
        } catch (Exception ex) {
            log.error("Failed to create command: ", ex);
            throw new CommandException("Failed to create command", ex);
        }

        for (Field field : commandClass.getDeclaredFields()) {
            if(field.isAnnotationPresent(CommandArgument.class)) {

                field.setAccessible(true);

                CommandArgument commandArgument = field.getAnnotation(CommandArgument.class);
                try {
                    fillField(field, command, args[commandArgument.index()]);
                } catch (IllegalAccessException e) {
                    log.error("parse: ", e);
                }
//                commandArgs.set(commandArgument.index(), field.get(command));

                field.setAccessible(false);
            }
        }

        return command;
    }
}
