package com.example.vnomobile.command;

import com.example.vnomobile.command.ascommands.COCommand;
import com.example.vnomobile.command.ascommands.CVCommand;
import com.example.vnomobile.command.ascommands.VNALCommand;
import com.example.vnomobile.command.servercommands.CTCommand;

public enum CommandType {

//    MS(MSCommand.class),
    CV(CVCommand.class),
    CO(COCommand.class),
    VNAL(VNALCommand.class),
    CT(CTCommand.class);

    CommandType(Class clazz) {
        this.commandClass = clazz;
    }


    public final Class commandClass;
}
