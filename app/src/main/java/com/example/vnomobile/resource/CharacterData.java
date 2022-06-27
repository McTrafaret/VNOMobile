package com.example.vnomobile.resource;

import com.example.vnomobile.exception.ResourceNotFoundException;
import com.example.vnomobile.util.FileUtil;

import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CharacterData {

    private final File charDirectory;

    private final String name;
    private final String showName;
    private final String mysteryName;
    private final String blipsFileName;

    private final File[] iniFiles;

    public CharacterData(File charDirectory) throws ResourceNotFoundException, IOException {
        this.charDirectory = charDirectory;

        File charIniFile = FileUtil.getCaseInsensitiveSubFile(charDirectory, "char.ini");
        if(charIniFile == null) {
            throw new ResourceNotFoundException(String.format("char.ini not found in %s", charDirectory.getPath()));
        }

        Wini charIni = new Wini(charIniFile);

        Profile.Section section = null;
        for(String sectionName : charIni.keySet()) {
            if(sectionName.toLowerCase().equals("options")) {
                section = charIni.get(sectionName);
                break;
            }
        }

        if(section == null) {
            this.name = charDirectory.getName();
            this.showName = "???";
            this.mysteryName = "???";
            this.blipsFileName = null;
        }
        else {
            this.name = section.get("name", String.class, charDirectory.getName());
            this.showName = section.get("showname", String.class, "???");
            this.mysteryName = section.get("mysteryname", String.class, "???");
            this.blipsFileName = section.get("blips", String.class, null);
        }


        this.iniFiles = charDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".ini") && !name.equals("char.ini");
            }
        });
        Arrays.sort(iniFiles, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public List<CharacterButton> getButtons(String name) {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getShowName() {
        return showName;
    }

    public String getMysteryName() {
        return mysteryName;
    }

    public String getBlipsFileName() {
        return blipsFileName;
    }

    public File[] getIniFiles() {
        return iniFiles;
    }
}
