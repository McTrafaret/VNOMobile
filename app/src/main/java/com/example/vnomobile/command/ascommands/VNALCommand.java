package com.example.vnomobile.command.ascommands;

import com.example.vnomobile.command.BaseCommand;
import com.example.vnomobile.command.Command;
import com.example.vnomobile.command.CommandType;

@Command(name = "VNAL", numOfArguments = 0)
public class VNALCommand extends BaseCommand {

    public VNALCommand() {
        super(CommandType.VNAL.toString());
    }
}
