package com.example.vnomobile.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.vnomobile.exception.ResourceNotFoundException;
import com.example.vnomobile.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataDirectory {

    private final String name;
    private final File directoryFile;


    public DataDirectory(String path) {
        this.directoryFile = new File(path);
        String[] files = path.split(File.separator);
        this.name = files[files.length-1];
    }

    public DataDirectory(File file) {
        this.directoryFile = file;
        String[] files = file.getAbsolutePath().split("/");
        this.name = files[files.length-1];
    }

    public Bitmap getMobileIcon() {
        File mobileIconFile = FileUtil.getCaseInsensitiveSubFile(directoryFile, "data/UI/mobile_icon.png");
        if(mobileIconFile != null && mobileIconFile.exists()) {
            return BitmapFactory.decodeFile(mobileIconFile.getPath());
        }
        return null;
    }

    public List<RosterImage> getRosterImages() throws ResourceNotFoundException {
        File rosterImageDirectory = FileUtil.getCaseInsensitiveSubFile(directoryFile, "data/misc/RosterImage");
        if(!rosterImageDirectory.exists() || !rosterImageDirectory.isDirectory()) {
            throw new ResourceNotFoundException("data/misc/RosterImage directory not found");
        }
        List<RosterImage> rosterImages = new ArrayList<>();
        String[] rosterImagesFileNames = rosterImageDirectory.list();
        if(rosterImagesFileNames == null) {
            throw new ResourceNotFoundException("No RosterImages found in \"data/misc/RosterImage\"");
        }
        Arrays.sort(rosterImagesFileNames);
        int i = 0;
        while(i < rosterImagesFileNames.length) {
            String offFileName = rosterImagesFileNames[i];
            Bitmap offIcon = BitmapFactory.decodeFile(rosterImageDirectory.getPath() + File.separator + offFileName);
            String characterName = offFileName.split("_")[0];
            Bitmap onIcon = null;
            if(rosterImagesFileNames[i + 1].startsWith(characterName)) {
                String onFileName = rosterImagesFileNames[i + 1];
                onIcon = BitmapFactory.decodeFile(rosterImageDirectory.getPath() + File.separator + onFileName);
                i++;
            }
            rosterImages.add(new RosterImage(characterName, onIcon, offIcon));
            i++;
        }

        return rosterImages;
    }

    public Bitmap getBigArt(String characterName) throws ResourceNotFoundException {
        File bigArtDirectory = FileUtil.getCaseInsensitiveSubFile(directoryFile, "data/misc/BigArt");
        if(!bigArtDirectory.exists() || !bigArtDirectory.isDirectory()) {
            throw new ResourceNotFoundException("data/misc/BigArt directory not found");
        }
        String[] bigArtFileNames = bigArtDirectory.list();
        if(bigArtFileNames == null) {
            return null;
        }
        for(String filename : bigArtDirectory.list())  {
            if(filename.toUpperCase().startsWith(characterName.toUpperCase())) {
                return BitmapFactory.decodeFile(bigArtDirectory.getPath() + File.separator + filename);
            }
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return directoryFile.getAbsolutePath();
    }
}
