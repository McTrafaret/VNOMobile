package com.example.vnomobile.resource;

import android.graphics.Bitmap;

public class RosterImage {

    private String characterName;
    private Bitmap onIcon;
    private Bitmap offIcon;

    public RosterImage(String characterName, Bitmap onIcon, Bitmap offIcon) {
        this.characterName = characterName;
        this.onIcon = onIcon;
        this.offIcon = offIcon;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Bitmap getOnIcon() {
        return onIcon;
    }

    public Bitmap getOffIcon() {
        return offIcon;
    }
}
