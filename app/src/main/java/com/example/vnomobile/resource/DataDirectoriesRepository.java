package com.example.vnomobile.resource;

import android.content.Context;
import android.net.Uri;

import com.example.vnomobile.exception.NotDataDirectoryException;
import com.example.vnomobile.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataDirectoriesRepository {

    private static String CACHE_FILE_NAME = "data_directories.txt";

    List<DataDirectory> directories;
    HashSet<String> directoriesHashSet;

    File cacheFile;

    public DataDirectoriesRepository(Context context) {
        this.directoriesHashSet = new HashSet<>();
        this.directories = new ArrayList<>();
        File cacheDir = context.getCacheDir();
        cacheFile = new File(cacheDir, CACHE_FILE_NAME);
    }

    public void loadDataFromCache() {
        parseCacheFile();
    }

    private void parseCacheFile() {
        try(FileReader fileReader = new FileReader(cacheFile)) {
            BufferedReader reader = new BufferedReader(fileReader);
            while(true) {
                String line = reader.readLine();
                if(line == null) {
                    break;
                }
                line = line.trim();
                if(!line.isEmpty()) {
                    if(!directoriesHashSet.contains(line)) {
                        directoriesHashSet.add(line);
                        directories.add(new DataDirectory(line));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.warn("{} doesn't exist, creating...", CACHE_FILE_NAME);
            try {
                cacheFile.createNewFile();
            } catch (IOException ioException) {
                log.error("Failed to create {}, not persisting...", CACHE_FILE_NAME);
            }
        } catch (IOException e) {
            log.error("While parsing {} :", CACHE_FILE_NAME, e);
        }
    }

    private void writeDirectories() {
        try(FileWriter writer = new FileWriter(cacheFile)) {
            for(DataDirectory directory : directories) {
                writer.write(String.format("%s%n", directory.getPath()));
            }
        } catch (FileNotFoundException e) {
            log.warn("{} doesn't exist, creating...", CACHE_FILE_NAME);
            try {
                cacheFile.createNewFile();
                writeDirectories();
            } catch (IOException ioException) {
                log.error("Failed to create {}, not persisting...", CACHE_FILE_NAME);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DataDirectory getDirectory(int i) {
        return directories.get(i);
    }

    public void addDirectory(String path) throws NotDataDirectoryException {
        if(path == null) {
            return;
        }
        File directory = new File(path);
        if(!directory.isDirectory()) {
            throw new NotDataDirectoryException(String.format("%s is not a directory", path));
        }
        File dataDir = new File(directory, "data");
        if(!dataDir.exists() || !dataDir.isDirectory()) {
            throw new NotDataDirectoryException(String.format("%s doesn't contain \"data\" directory", path));
        }
        if(!directoriesHashSet.contains(path)) {
            directoriesHashSet.add(path);
            directories.add(new DataDirectory(directory));
            writeDirectories();
        }
    }

    public int getSize() {
        return directories.size();
    }
}
