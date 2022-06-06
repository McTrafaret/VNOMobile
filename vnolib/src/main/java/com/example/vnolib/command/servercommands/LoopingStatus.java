package com.example.vnolib.command.servercommands;

public enum LoopingStatus {
    NOT_LOOPING(0),
    LOOPING(1),
    ZALOOPING(696969);

    private final int statusNum;

    LoopingStatus(int statusNum) {
        this.statusNum = statusNum;
    }

    public boolean isLooping() {
        return statusNum != 0;
    }
}
