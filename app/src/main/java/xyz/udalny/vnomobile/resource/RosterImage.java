package xyz.udalny.vnomobile.resource;

import java.io.File;

public class RosterImage {

    private String characterName;
    private File onIconFile;
    private File offIconFile;

    public RosterImage(String characterName, File onIconFile, File offIconFile) {
        this.characterName = characterName;
        this.onIconFile = onIconFile;
        this.offIconFile = offIconFile;
    }

    public String getCharacterName() {
        return characterName;
    }

    public File getOnIconFile() {
        return onIconFile;
    }

    public File getOffIconFile() {
        return offIconFile;
    }
}
