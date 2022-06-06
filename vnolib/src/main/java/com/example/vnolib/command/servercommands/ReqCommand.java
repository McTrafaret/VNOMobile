package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * This command is sent by client to pick a character.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "Req", numOfArguments = 2)
public class ReqCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    int characterId;

    @CommandArgument(index = 1)
    String password;

    public ReqCommand(int characterId) {
        this.characterId = characterId;
        this.password = "";
    }
}
