package com.example.vnolib.command;

import com.example.vnolib.exception.CommandException;

import junit.framework.TestCase;

public class CommandParserTest extends TestCase {

    public void testParse() throws CommandException {
        System.out.println(CommandParser.parse("MS#Bernkastel#8#Хоть сейчас он и не Инквизитор...#char#0#20#Verande_Meta1.png#3#0##%"));
    }
}