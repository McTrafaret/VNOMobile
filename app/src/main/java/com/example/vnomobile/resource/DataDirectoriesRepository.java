package com.example.vnomobile.resource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataDirectoriesRepository {

    private static DataDirectoriesRepository instance;

    List<String> directoriesPaths;
    HashSet<String> directoriesHashSet;

    private DataDirectoriesRepository() {
        this.directoriesHashSet = new HashSet<>();
        this.directoriesPaths = new ArrayList<>();
    }

    public static DataDirectoriesRepository getInstance() {
        if(instance == null) {
            instance = new DataDirectoriesRepository();
        }
        return instance;
    }

    public String get(int i) {
        return directoriesPaths.get(i);
    }

    public void add(String path) {
        if(!directoriesHashSet.contains(path)) {
            directoriesHashSet.add(path);
            directoriesPaths.add(path);
        }
    }

    public int size() {
        return directoriesPaths.size();
    }
}
