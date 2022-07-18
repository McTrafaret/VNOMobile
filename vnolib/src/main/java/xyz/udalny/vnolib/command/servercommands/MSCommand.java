package xyz.udalny.vnolib.command.servercommands;

import xyz.udalny.vnolib.command.BaseCommand;
import xyz.udalny.vnolib.command.Command;
import xyz.udalny.vnolib.command.CommandArgument;
import xyz.udalny.vnolib.command.servercommands.enums.MessageColor;
import xyz.udalny.vnolib.command.servercommands.enums.SpriteFlip;
import xyz.udalny.vnolib.command.servercommands.enums.SpritePosition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
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

    @Setter
    @CommandArgument(index = 6, optional = false)
    String backgroundImageName;

    @CommandArgument(index = 7, optional = false)
    SpritePosition position;

    @CommandArgument(index = 8, optional = false)
    SpriteFlip flip;

    @CommandArgument(index = 9, optional = false)
    String sfx;
}
