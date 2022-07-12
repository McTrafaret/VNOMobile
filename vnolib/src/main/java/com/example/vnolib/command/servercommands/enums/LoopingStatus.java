package com.example.vnolib.command.servercommands.enums;

public enum LoopingStatus implements CommandEnum {
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

    public static LoopingStatus fromBoolean(boolean status) {
        if(status) {
            return LOOPING;
        }
        return NOT_LOOPING;
    }

    @Override
    public String asRequestArgument() {
        return Integer.toString(statusNum);
    }
}
