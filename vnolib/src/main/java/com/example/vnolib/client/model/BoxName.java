package com.example.vnolib.client.model;

public enum BoxName {

    CHARACTER_NAME("char"),
    MYSTERYNAME("$ALT"),
    USERNAME(null);

    String requestString;

    BoxName(String requestString) {
        this.requestString = requestString;
    }

    public String getRequestString() {
        return requestString;
    }
}
