package xyz.udalny.vnomobile.render;

import java.io.File;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import xyz.udalny.vnolib.command.servercommands.enums.SpriteFlip;
import xyz.udalny.vnolib.command.servercommands.enums.SpritePosition;
import xyz.udalny.vnomobile.resource.sprite.Sprite;

@Builder
@Getter
public class RenderModel {

    @Getter
    public static class SpriteDrawInfo {
        private Sprite sprite;
        private SpritePosition position;
        private SpriteFlip flip;

        public SpriteDrawInfo(Sprite sprite, SpritePosition position, SpriteFlip flip) {
            this.sprite = sprite;
            this.position = position;
            this.flip = flip;
        }
    }

    @Setter
    private RenderState state;

    private File backgroundFile;
    private File textBoxFile;
    private List<SpriteDrawInfo>  spriteDrawInfo;
    private String boxName;
    private int textColor;

    @Setter
    private String text;
}
