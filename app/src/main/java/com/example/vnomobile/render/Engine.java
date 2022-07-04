package com.example.vnomobile.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceView;

import com.example.vnolib.client.model.BoxName;
import com.example.vnolib.command.servercommands.MSCommand;
import com.example.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnomobile.ClientHandler;
import com.example.vnomobile.resource.CharacterData;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.Sprite;
import com.example.vnomobile.resource.UIDesign;
import com.example.vnomobile.util.UIUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class Engine {

    private final SurfaceView surfaceView;
    private final DataDirectory dataDirectory;
    private final UIDesign design;

    private final HashMap<String, Positions> backgroundToPositionsMap = new HashMap<>();

    @Setter
    @Getter
    private class Positions {
        Sprite leftSprite;
        Sprite rightSprite;
    }

    public Engine(SurfaceView surfaceView, DataDirectory dataDirectory, UIDesign design) {
        this.surfaceView = surfaceView;
        this.dataDirectory = dataDirectory;
        this.design = design;
    }

    public void render(MSCommand command) {
        //command.get

        // render background
        // render characters
        // render chatbox
        // render text

        Bitmap background = dataDirectory.getBackground(command.getBackgroundImageName());
        if(background == null) {
            background = design.getBackdrop();
        }
        Sprite sprite = dataDirectory.getSprite(command.getCharacterName(), command.getSpriteName());
        Bitmap chatbox = design.getChatBox();

        Canvas canvas = new Canvas();
        canvas.drawBitmap(background, 0, 0, null);
        canvas.drawBitmap(sprite.getSpriteBitmap(), 0, 0, null);
        canvas.drawBitmap(chatbox, 0, canvas.getHeight() - chatbox.getHeight(), null);
    }

    public void handle(MSCommand command) {
        CharacterData characterData = dataDirectory.getCharacterData(command.getCharacterName());
        BoxName boxName = BoxName.fromString(command.getBoxName());
        String nameToShow = null;
        switch (boxName) {
            case CHARACTER_NAME:
                nameToShow = characterData.getShowName();
                break;
            case MYSTERYNAME:
                nameToShow = characterData.getMysteryName();
                break;
            case USERNAME:
                nameToShow = ClientHandler.getClient().getUsername();
                break;
        }
        Sprite sprite = dataDirectory.getSprite(command.getCharacterName(), command.getSpriteName());

        if(!backgroundToPositionsMap.containsKey(command.getBackgroundImageName())) {
            backgroundToPositionsMap.put(command.getBackgroundImageName(), new Positions());
        }

        Positions positions = backgroundToPositionsMap.get(command.getBackgroundImageName());
        if(command.getPosition().equals(SpritePosition.LEFT)) {
            positions.setLeftSprite(sprite);
        }
        else if(command.getPosition().equals(SpritePosition.RIGHT)) {
            positions.setRightSprite(sprite);
        }

        List<RenderModel.SpriteDrawInfo> infoList = new LinkedList<>();


        RenderModel model = RenderModel.builder()
                .boxName(nameToShow)
                .text(command.getMessage())
                .textColor(surfaceView.getResources().getColor(UIUtil.getColorId(command.getMessageColor())))
                .textBox(design.getChatBox())
                .background(dataDirectory.getBackground(command.getBackgroundImageName()))
                .spriteDrawInfo()
                .build();
    }
}
