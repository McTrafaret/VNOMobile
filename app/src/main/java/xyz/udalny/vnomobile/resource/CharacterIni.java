package xyz.udalny.vnomobile.resource;

import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

import xyz.udalny.vnomobile.util.FileUtil;

public class CharacterIni {

    private final File iniFile;
    private final CharacterButton[] buttons;

    private static File getSfxFile(File iniFile, String sfxFileName) {
        File dataDirectory = iniFile.getParentFile().getParentFile().getParentFile();
        File sfxDirectory = FileUtil.getCaseInsensitiveSubFile(dataDirectory, "sounds/sfx");
        String[] fileNames = sfxDirectory.list();
        if(fileNames == null)
            return null;
        for(String filename : fileNames) {
            if(filename.toLowerCase().startsWith(sfxFileName.toLowerCase())) {
                return new File(sfxDirectory, filename);
            }
        }
        return null;
    }

    private static File getButtonFile(File iniFile, String buttonFileName) {
        File buttonsDirectory = FileUtil.getCaseInsensitiveSubFile(iniFile.getParentFile(), "buttons");
        for (String filename : buttonsDirectory.list()) {
            if(filename.toLowerCase().startsWith(buttonFileName.toLowerCase())) {
                return new File(buttonsDirectory, filename);
            }
        }
        return null;
    }

    public CharacterIni(File iniFile) throws IOException {
        this.iniFile = iniFile;

        Wini wini = new Wini(iniFile);

        Profile.Section emotionsSection = wini.get("Emotions");
        Profile.Section sfxSection = wini.get("Sfx");

        int numOfButtons = emotionsSection.get("number", int.class);
        buttons = new CharacterButton[numOfButtons];

        for(String key : emotionsSection.keySet()) {
            int index;
            try {
                index = Integer.parseInt(key);
            } catch (Exception ex) {
                continue;
            }
            if(index > numOfButtons) {
                continue;
            }
            File sfxFile = null;
            if(sfxSection != null && sfxSection.containsKey(key)) {
                String sfxFileName = sfxSection.get(key);
                sfxFile = getSfxFile(iniFile, sfxFileName);
            }
            File buttonFile = getButtonFile(iniFile, emotionsSection.get(key));
            buttons[index-1] = new CharacterButton(buttonFile, sfxFile);
        }
    }

    public CharacterButton[] getButtons() {
        return buttons;
    }
}
