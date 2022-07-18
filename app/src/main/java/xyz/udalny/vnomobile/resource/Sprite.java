package xyz.udalny.vnomobile.resource;

import java.io.File;

public class Sprite {

    private final String name;
    private final File spriteFile;

    public Sprite(File spriteFile) {
        this.spriteFile = spriteFile;
        this.name = spriteFile.getName().split("\\.")[0];
    }

    public String getName() {
        return name;
    }

    public File getSpriteFile() {
        return spriteFile;
    }
}
