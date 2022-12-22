package xyz.udalny.vnomobile.resource.sprite;

import android.graphics.Bitmap;
import android.view.View;

import java.io.File;

import xyz.udalny.vnomobile.util.FileUtil;

public class Sprite {

    protected String name;
    protected File spriteFile;

    public Sprite() {
    }

    public Sprite(File spriteFile) {
        this.spriteFile = spriteFile;
        this.name = spriteFile.getName().split("\\.")[0];
    }

    public String getName() {
        return name;
    }

    public Bitmap getBitmap(View view) {
        return FileUtil.loadBitmapFromFile(view, spriteFile);
    }
}
