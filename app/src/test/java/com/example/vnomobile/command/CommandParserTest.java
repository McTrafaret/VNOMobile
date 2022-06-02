package com.example.vnomobile.command;

import com.example.vnomobile.exception.CommandException;

import junit.framework.TestCase;

public class CommandParserTest extends TestCase {

    public void testParse() throws CommandException {
        String commandString = "CT#chel#blablabla#%";
        BaseCommand command = CommandParser.parse(commandString);
        System.out.println(command.toVnoString());
        assertEquals(command.toVnoString(), commandString);
    }
}