package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Command(name = "GmB", numOfArguments = 1)
public class GmBRequestCommand {

    @CommandArgument(index = 0, optional = false)
    int modId;
}
