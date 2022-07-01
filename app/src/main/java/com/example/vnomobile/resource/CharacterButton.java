package com.example.vnomobile.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class CharacterButton {

    private final File buttonFile;
    private final File sfxFile;
    private final Bitmap buttonBitmap;

    public CharacterButton(File buttonFile, File sfxFile) {
        this.buttonFile = buttonFile;
        this.sfxFile = sfxFile;
        this.buttonBitmap = BitmapFactory.decodeFile(buttonFile.getPath());
    }

    public Bitmap getButtonBitmap() {
        return buttonBitmap;
    }

    public String getAssociatedSpriteName() {
        return buttonFile.getName().split("\\.")[0];
    }
}
