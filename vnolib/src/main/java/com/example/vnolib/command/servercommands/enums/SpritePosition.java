package com.example.vnolib.command.servercommands.enums;

public enum SpritePosition implements CommandEnum {
    LEFT(1),
    RIGHT(2),
    CENTER(3);

    public final int positionIndex;

    SpritePosition(int index) {
        this.positionIndex = index;
    }

    @Override
    public String asRequestArgument() {
        return Integer.toString(positionIndex);
    }
}
