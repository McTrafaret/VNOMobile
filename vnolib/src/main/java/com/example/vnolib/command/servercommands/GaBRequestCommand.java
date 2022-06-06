package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Command(name = "GaB", numOfArguments = 1)
public class GaBRequestCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int animatorId;

}
