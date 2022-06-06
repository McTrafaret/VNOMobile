package com.example.vnolib.command.servercommands;

public enum SpritePosition {
    LEFT(1),
    RIGHT(2),
    CENTER(3);

    public final int positionIndex;

    SpritePosition(int index) {
        this.positionIndex = index;
    }
}
