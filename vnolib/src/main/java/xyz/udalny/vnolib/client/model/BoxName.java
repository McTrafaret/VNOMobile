package xyz.udalny.vnolib.client.model;

public enum BoxName {

    CHARACTER_NAME("char"),
    MYSTERYNAME("$ALT"),
    USERNAME(null);

    String requestString;

    BoxName(String requestString) {
        this.requestString = requestString;
    }

    public static BoxName fromString(String boxName) {
        if(boxName.equalsIgnoreCase("char")) {
            return CHARACTER_NAME;
        }
        else if(boxName.equalsIgnoreCase("$ALT")) {
            return MYSTERYNAME;
        }
        return USERNAME;
    }

    public String getRequestString() {
        return requestString;
    }
}
