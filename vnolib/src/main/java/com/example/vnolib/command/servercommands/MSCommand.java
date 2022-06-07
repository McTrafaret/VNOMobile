package com.example.vnolib.command.servercommands;

import com.example.vnolib.command.BaseCommand;
import com.example.vnolib.command.Command;
import com.example.vnolib.command.CommandArgument;
import com.example.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Command(name = "MS", numOfArguments = 10)
public class MSCommand extends BaseCommand {

    @CommandArgument(index = 0, optional = false)
    String characterName;

    @CommandArgument(index = 1, optional = false)
    String spriteName;

    @CommandArgument(index = 2, optional = false)
    String message;

    @CommandArgument(index = 3, optional = false)
    String boxName;

    @CommandArgument(index = 4, optional = false)
    MessageColor messageColor;

    @CommandArgument(index = 5, optional = false)
    int characterId;

    @CommandArgument(index = 6, optional = false)
    String backgroundImageName;

    @CommandArgument(index = 7, optional = false)
    SpritePosition position;

    @CommandArgument(index = 8, optional = false)
    SpriteFlip flip;

    @CommandArgument(index = 9, optional = false)
    String sfx;
}
