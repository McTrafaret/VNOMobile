package xyz.udalny.vnomobile.resource;

public class ResourceHandler {

    private static ResourceHandler instance;

    private DataDirectory directory;

    private ResourceHandler() {
    }

    public static ResourceHandler getInstance() {
        if(instance == null) {
            instance = new ResourceHandler();
        }
        return instance;
    }

    public void init(DataDirectory dataDirectory) {
        this.directory = dataDirectory;
    }

    public DataDirectory getDirectory() {
        return directory;
    }
}
