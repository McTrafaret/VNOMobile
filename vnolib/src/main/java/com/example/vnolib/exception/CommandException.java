package com.example.vnolib.exception;

public class CommandException extends Exception {

    public CommandException(String s) {
        super(s);
    }

    public CommandException(String s, Throwable ex) {
        super(s, ex);
    }
}
