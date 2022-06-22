package com.example.vnolib.client.model;

import com.example.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterState {

    BoxName boxName;
    String spriteName;
    String backgroundName;
    MessageColor messageColor;
    SpritePosition position;
    SpriteFlip flip;
    String sfx;

    public CharacterState() {
        this.boxName = BoxName.CHARACTER_NAME;
        this.spriteName = null;
        this.backgroundName = null;
        this.messageColor = MessageColor.WHITE;
        this.position = SpritePosition.LEFT;
        this.flip = SpriteFlip.NOFLIP;
        this.sfx = null;
    }
}
