package com.example.vnomobile.command;

import android.util.Log;

import com.example.vnomobile.exception.CommandException;

import java.lang.reflect.Field;

public abstract class BaseCommand {

    private String name;

    public BaseCommand(String name) {
        this.name = name;
    }

    public String toVnoString() throws CommandException {
        Class thisClass = this.getClass();
        if(!thisClass.isAnnotationPresent(Command.class)) {
            throw new CommandException("Command is not annotated.");
        }

        Command commandAnnotation = (Command) thisClass.getAnnotation(Command.class);
        Object[] args = new Object[commandAnnotation.numOfArguments()];

        for(Field field : thisClass.getDeclaredFields()) {
            if(field.isAnnotationPresent(CommandArgument.class)) {

                field.setAccessible(true);


                CommandArgument commandArgument = field.getAnnotation(CommandArgument.class);
                try {
                    args[commandArgument.index()] = field.get(this);
                } catch (Exception ex) {
                    Log.e("COMMAND", "Failed to convert to VNOString: ", ex);
                }

                field.setAccessible(false);
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(name);
        for(Object arg : args) {
            builder.append("#");
            builder.append(arg.toString());
        }
        builder.append("#%");

        return builder.toString();

//        StringBuilder builder = new StringBuilder();
//        builder.append(name);
//        for (Object arg : args) {
//            builder.append("#");
//            builder.append(arg.toString());
//        }
//        builder.append("#%");
//        return builder.toString();
    }
}
