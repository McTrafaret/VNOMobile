package com.example.vnomobile.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.vnolib.client.model.Character;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnomobile.exception.ResourceNotFoundException;
import com.example.vnomobile.util.FileUtil;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
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

    public Bitmap getBigArt(Character character) throws ResourceNotFoundException {
        File bigArtDirectory = FileUtil.getCaseInsensitiveSubFile(directoryFile, "data/misc/BigArt");
        if(!bigArtDirectory.exists() || !bigArtDirectory.isDirectory()) {
            throw new ResourceNotFoundException("data/misc/BigArt directory not found");
        }
        String[] bigArtFileNames = bigArtDirectory.list();
        if(bigArtFileNames == null) {
            return null;
        }
        for(String filename : bigArtFileNames)  {
            if(filename.toUpperCase().startsWith(character.getCharName().toUpperCase())) {
                return BitmapFactory.decodeFile(bigArtDirectory.getPath() + File.separator + filename);
            }
        }

        return null;
    }

    public CharacterData getCharacterData(Character character) throws ResourceNotFoundException, IOException {
        File charactersDirectory = FileUtil.getCaseInsensitiveSubFile(directoryFile, "data/characters");
        if(!charactersDirectory.exists() || !charactersDirectory.isDirectory()) {
            throw new ResourceNotFoundException("data/characters directory not found");
        }
        String[] charactersFileNames = charactersDirectory.list();
        if(charactersFileNames == null) {
            return null;
        }
        for(String filename : charactersFileNames) {
            if(filename.toUpperCase().startsWith(character.getCharName().toUpperCase())) {
                return new CharacterData(new File(charactersDirectory.getPath() + File.separator + filename));
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

    public Sprite getSprite(String characterName, String spriteName) {
        String pathToCharacterDir = String.format("data/characters/%s", characterName);
        File characterDirectory = FileUtil.getCaseInsensitiveSubFile(directoryFile, pathToCharacterDir);
        if(characterDirectory == null) {
            return null;
        }
        for (String filename : characterDirectory.list()) {
            if(filename.toLowerCase().startsWith(spriteName.toLowerCase())) {
                return new Sprite(new File(characterDirectory, filename));
            }
        }
        return null;
    }

    public UIDesign getDesign(String designName) {
        String pathToDesign = String.format("data/UI/%s", designName);
        File designDirectory = FileUtil.getCaseInsensitiveSubFile(directoryFile, pathToDesign);
        return new UIDesign(designDirectory);
    }

    public Wini getSettings() throws IOException {
        File settingFile = FileUtil.getCaseInsensitiveSubFile(directoryFile, "data/settings.ini");
        return new Wini(settingFile);
    }

}
