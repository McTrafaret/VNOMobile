package com.example.vnolib.command.servercommands;

public enum SpriteFlip {
    NOFLIP(0),
    FLIP(1);

    private final int flipNum;

    SpriteFlip(int flip) {
        flipNum = flip;
    }

    public boolean hasFlip() {
        return flipNum != 0;
    }
}
