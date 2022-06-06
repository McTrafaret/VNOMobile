package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MODCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    private static final String argument1 = "AUTH";

    @CommandArgument(index = 1, optional = false)
    private String password;
}
