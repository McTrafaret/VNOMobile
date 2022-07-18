package xyz.udalny.vnolib.command.servercommands.enums;

public enum MessageColor implements CommandEnum {
    WHITE(0),
    BLUE(1),
    PINK(2),
    YELLOW(3),
    GREEN(4),
    ORANGE(5),
    RED(6);

    public final int colorIndex;

    MessageColor(int index) {
        colorIndex = index;
    }

    public static MessageColor fromInt(int a) {
        for(MessageColor value : MessageColor.values()) {
            if (value.colorIndex == a) {
                return value;
            }
        }
        return null;
    }

    @Override
    public String asRequestArgument() {
        return Integer.toString(colorIndex);
    }
}
