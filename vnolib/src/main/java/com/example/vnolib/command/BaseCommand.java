package com.example.vnolib.command;


import com.example.vnolib.client.Client;
import com.example.vnolib.command.servercommands.enums.CommandEnum;
import com.example.vnolib.exception.CommandException;

import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseCommand {

    private String name;

    public BaseCommand() {
        Class<? extends BaseCommand> commandClass = this.getClass();
        Command commandAnnotation = commandClass.getAnnotation(Command.class);
        this.name = commandAnnotation.name();
    }

    public void handle(Client client) {

    }

    public String toVnoString() throws CommandException {
        Class<? extends BaseCommand> thisClass = this.getClass();
        if (!thisClass.isAnnotationPresent(Command.class)) {
            throw new CommandException("Command is not annotated.");
        }

        Command commandAnnotation = thisClass.getAnnotation(Command.class);
        Object[] args = new Object[commandAnnotation.numOfArguments()];

        for (Field field : thisClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(CommandArgument.class)) {

                field.setAccessible(true);


                CommandArgument commandArgument = field.getAnnotation(CommandArgument.class);
                try {
                    args[commandArgument.index()] = field.get(this);
                } catch (Exception ex) {
                    log.error("toVnoString: ", ex);
                }

                field.setAccessible(false);
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name);
        for (Object arg : args) {
            builder.append("#");
            if(CommandEnum.class.isAssignableFrom(arg.getClass())) {
                CommandEnum commandEnum = (CommandEnum) arg;
                builder.append(commandEnum.asRequestArgument());
                continue;
            }
            builder.append(
                    arg.toString()
                    .replace("#", "<pound>")
                    .replace("%", "<percent>"));
        }
        builder.append("#%");

        return builder.toString();
    }
}
