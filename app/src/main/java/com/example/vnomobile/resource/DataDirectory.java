package com.example.vnomobile.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class DataDirectory {

    private String name;
    private String path;

    public DataDirectory(String path) {
        this.path = path;
        String[] files = path.split(File.separator);
        this.name = files[files.length-1];
    }

    public Bitmap getMobileIcon() {
        File mobileIconFile = new File(path, "data/UI/mobile_icon.png");
        if(mobileIconFile.exists()) {
            return BitmapFactory.decodeFile(mobileIconFile.getPath());
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }
}
