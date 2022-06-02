package com.example.vnomobile.exception;

public class CommandException extends Exception {

    public CommandException(String s) {
        super(s);
    }

    public CommandException(String s, Throwable ex) {
        super(s, ex);
    }
}
