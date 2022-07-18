package xyz.udalny.vnolib.command.servercommands.enums;

public enum SpritePosition implements CommandEnum {
    LEFT(1),
    RIGHT(2),
    CENTER(3);

    public final int positionIndex;

    SpritePosition(int index) {
        this.positionIndex = index;
    }

    @Override
    public String asRequestArgument() {
        return Integer.toString(positionIndex);
    }

    public SpritePosition nextPosition() {
        switch (this) {
            case LEFT: return RIGHT;
            case RIGHT: return CENTER;
            case CENTER: return LEFT;
        }
        return null;
    }
}
