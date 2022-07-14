package com.example.vnomobile.resource;

import java.io.File;

public class CharacterButton {

    private final File buttonFile;
    private final File sfxFile;

    public CharacterButton(File buttonFile, File sfxFile) {
        this.buttonFile = buttonFile;
        this.sfxFile = sfxFile;
    }

    public File getButtonFile() {
        return buttonFile;
    }

    public String getAssociatedSpriteName() {
        return buttonFile.getName().split("\\.")[0];
    }
}
