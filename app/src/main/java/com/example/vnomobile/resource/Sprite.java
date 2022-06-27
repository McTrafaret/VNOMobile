package com.example.vnomobile.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Sprite {

    private final String name;
    private final File spriteFile;
    private final Bitmap spriteBitmap;

    public Sprite(File spriteFile) {
        this.spriteFile = spriteFile;
        this.name = spriteFile.getName().split("\\.")[0];
        this.spriteBitmap = BitmapFactory.decodeFile(spriteFile.getPath());
    }

    public String getName() {
        return name;
    }

    public Bitmap getSpriteBitmap() {
        return spriteBitmap;
    }
}
