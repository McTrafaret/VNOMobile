package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;
import com.example.vnolib.command.servercommands.enums.LoopingStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "MC", numOfArguments = 5)
public class MCCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String characterName;

    @CommandArgument(index = 1, optional = false)
    String trackName;

    @CommandArgument(index = 2, optional = false)
    int trackId;

    @CommandArgument(index = 3, optional = false)
    int characterId;

    @CommandArgument(index = 4, optional = false)
    LoopingStatus loopingStatus;
}
