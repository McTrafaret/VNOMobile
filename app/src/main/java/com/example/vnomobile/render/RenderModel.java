package com.example.vnomobile.render;

import com.example.vnolib.command.servercommands.enums.SpritePosition;

import java.io.File;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class RenderModel {

    @Getter
    public static class SpriteDrawInfo {
        private File spriteFile;
        private SpritePosition position;

        public SpriteDrawInfo(File spriteFile, SpritePosition position) {
            this.spriteFile = spriteFile;
            this.position = position;
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
