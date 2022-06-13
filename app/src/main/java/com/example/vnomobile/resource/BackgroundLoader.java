package com.example.vnomobile.resource;

import com.example.vnolib.client.model.Area;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BackgroundLoader {

    private final File backgroundsDirectory;
    private final Map<String, Resource> resourceMap;
    private Area currentArea;

    public BackgroundLoader(File backgroundsDirectory) throws IOException {
        resourceMap = new HashMap<>();
        if(!backgroundsDirectory.isDirectory()) {
            throw new IOException("File is not a directory");
        }
        this.backgroundsDirectory = backgroundsDirectory;
    }

    public Resource getBackground(String name) {
        return resourceMap.containsKey(name) ? resourceMap.get(name) : null;
    }

    private void loadArea(Area area) {
        resourceMap.clear();
        for(File background : backgroundsDirectory.listFiles()) {
            if(background.getName().contains(area.getBackgroundNamePattern())) {
                try(FileInputStream fileInputStream = new FileInputStream(background)) {
                    byte[] bytes = new byte[(int) background.length()];
                    fileInputStream.read(bytes);
                    resourceMap.put(background.getName(), new Resource(bytes));
                } catch (IOException e) {
                    log.error(String.format("Failed to load file %s while loading area %s: ", background.getName(), area.getLocationName()), e);
                }
            }
        }
        currentArea = area;
    }
}
