package com.example.vnomobile.render;

import android.graphics.Bitmap;

import com.example.vnolib.command.servercommands.enums.SpritePosition;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class RenderModel {

    @Getter
    public static class SpriteDrawInfo {
        private Bitmap spriteBitmap;
        private SpritePosition position;

        public SpriteDrawInfo(Bitmap spriteBitmap, SpritePosition position) {
            this.spriteBitmap = spriteBitmap;
            this.position = position;
        }
    }

    private Bitmap background;
    private Bitmap textBox;
    private List<SpriteDrawInfo>  spriteDrawInfo;
    private String boxName;
    private int textColor;

    @Setter
    private String text;
}
