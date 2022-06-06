package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * This command is sent by the client to server to change area.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "ARC", numOfArguments = 2)
public class ARCCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int areadId;

    /**
     * I don't know the purpose of this field. All packets i got had it empty.
     * Maybe it is some kind of password and for regular locations it is empty.
     */
    @CommandArgument(index = 1)
    String arg2;
}
