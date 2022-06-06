package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;

//TODO:
// this command have either 3 or 6 arguments depending
// on whether the requested char is last or not
// need to write SPECIFIC logic for parsing in CommandParser
@Command(name = "CAD", numOfArguments = 6)
public class CADCommand extends BaseCommand {
}
