package com.example.vnomobile.resource;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnomobile.util.FileUtil;

import java.io.File;
import java.util.EnumMap;

import lombok.Getter;

@Getter
public class UIDesign {

    private final File designDirectory;
    private final EnumMap<SpritePosition, Bitmap> positionToBitmapMap;
    private final EnumMap<SpriteFlip, Bitmap> flipToBitmapMap;
    private final Bitmap[] sfxButtons;
    private final Bitmap[] emoteSelectBitmaps;
//    private final Bitmap arrow;
    private final Bitmap backdrop;
    private final Bitmap chatBox;

    public UIDesign(File designDirectory) {
        this.designDirectory = designDirectory;
        this.positionToBitmapMap = createPositionToButtonBitmapMap(designDirectory);
        this.flipToBitmapMap = createFlipToButtonBitmapMap(designDirectory);
        this.sfxButtons = retrieveSfxButtons(designDirectory);
        this.emoteSelectBitmaps = retrieveEmoteSelectBitmaps(designDirectory);
        this.backdrop = retrieveBackdrop(designDirectory);
        this.chatBox = retrieveChatBox(designDirectory);
    }

    private static EnumMap<SpritePosition, Bitmap> createPositionToButtonBitmapMap(File designDirectory) {
        File UIButtonsDirectory = FileUtil.getCaseInsensitiveSubFile(designDirectory, "buttons");
        EnumMap<SpritePosition, Bitmap> map = new EnumMap<>(SpritePosition.class);
        File leftFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "side");
        File centerFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "side_mid");
        File rightFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "side_on");

        // TODO : add checks if any of this files exist and replace with some default bitmaps maybe
        map.put(SpritePosition.LEFT, BitmapFactory.decodeFile(leftFile.getPath()));
        map.put(SpritePosition.CENTER, BitmapFactory.decodeFile(centerFile.getPath()));
        map.put(SpritePosition.RIGHT, BitmapFactory.decodeFile(rightFile.getPath()));
        return map;
    }

    private static EnumMap<SpriteFlip, Bitmap> createFlipToButtonBitmapMap(File designDirectory) {
        EnumMap<SpriteFlip, Bitmap> map = new EnumMap<>(SpriteFlip.class);
        File UIButtonsDirectory = FileUtil.getCaseInsensitiveSubFile(designDirectory, "buttons");
        File noFlipFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "mirror");
        File flipFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "mirror_on");

        map.put(SpriteFlip.NOFLIP, BitmapFactory.decodeFile(noFlipFile.getPath()));
        map.put(SpriteFlip.FLIP, BitmapFactory.decodeFile(flipFile.getPath()));
        return map;
    }

    private static Bitmap[] retrieveSfxButtons(File designDirectory) {
        Bitmap[] bitmaps = new Bitmap[2];
        File UIButtonsDirectory = FileUtil.getCaseInsensitiveSubFile(designDirectory, "buttons");
        File sfxOnFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "sfx_on");
        File sfxOffFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "sfx_off");

        bitmaps[0] = BitmapFactory.decodeFile(sfxOffFile.getPath());
        bitmaps[1] = BitmapFactory.decodeFile(sfxOnFile.getPath());

        return bitmaps;
    }

    private static Bitmap[] retrieveEmoteSelectBitmaps(File designDirectory) {
        Bitmap[] bitmaps = new Bitmap[2];
        File emoteSelectedFile = FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "emoteselected");
        File emoteUnselectedFile = FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "emoteunselected");

        bitmaps[0] = BitmapFactory.decodeFile(emoteUnselectedFile.getPath());
        bitmaps[1] = BitmapFactory.decodeFile(emoteSelectedFile.getPath());

        return bitmaps;
    }

    // TODO: arrow is a gif, so we need to think this
    private static Bitmap retrieveArrow(File designDirectory) {
        return null;
    }

    private static Bitmap retrieveBackdrop(File designDirectory) {
        File backdropFile = FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "backdrop");
        if(backdropFile == null) {
            return null;
        }
        return BitmapFactory.decodeFile(backdropFile.getPath());
    }

    private static Bitmap retrieveChatBox(File designDirectory) {
        File chatboxFile = FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "chat");
        if(chatboxFile == null) {
            return null;
        }
        return BitmapFactory.decodeFile(chatboxFile.getPath());
    }
}
