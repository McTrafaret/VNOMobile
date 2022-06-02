package com.example.vnomobile.command.ascommands;

import com.example.vnomobile.command.BaseCommand;
import com.example.vnomobile.command.Command;
import com.example.vnomobile.command.CommandType;

@Command(name = "CV", numOfArguments = 0)
public class CVCommand extends BaseCommand {

    public CVCommand() {
        super(CommandType.CV.toString());
    }
}
