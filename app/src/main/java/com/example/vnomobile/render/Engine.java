package com.example.vnomobile.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.vnolib.client.model.BoxName;
import com.example.vnolib.command.servercommands.MSCommand;
import com.example.vnolib.command.servercommands.enums.SpriteFlip;
import com.example.vnolib.command.servercommands.enums.SpritePosition;
import com.example.vnomobile.resource.CharacterData;
import com.example.vnomobile.resource.DataDirectory;
import com.example.vnomobile.resource.SoundHandler;
import com.example.vnomobile.resource.Sprite;
import com.example.vnomobile.resource.UIDesign;
import com.example.vnomobile.util.GifUtil;
import com.example.vnomobile.util.UIUtil;

import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Engine {

    private static final int TEXT_SPEED = 20;

    private final SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;
    private SurfaceHolder.Callback surfaceCallback;

    private final DataDirectory dataDirectory;
    private final UIDesign design;
    private final Render render;

    private final SoundHandler soundHandler;

    private final RunThread runThread;

    private final Lock modelLock = new ReentrantLock();

    private String bleepName;

    private String sfxName;
    private boolean sfxPlayed = false;

    private String currentMessage;
    private RenderModel currentModelWithoutMessage;
    private volatile boolean modelChanged = false;

    private volatile boolean stopped = false;

    private final HashMap<String, Positions> backgroundToPositionsMap = new HashMap<>();

    @Setter
    @Getter
    private class Positions {
        Sprite leftSprite;
        SpriteFlip leftSpriteFlip;
        Sprite rightSprite;
        SpriteFlip rightSpriteFlip;
    }

    private class RunThread extends Thread {

        private int lastShownLetterIndex = 0;

        @Override
        public void run() {
            while (!stopped) {
                Canvas canvas = null;
                if (surfaceHolder == null || currentModelWithoutMessage == null || (canvas = surfaceHolder.lockCanvas()) == null ) {
                    synchronized (Engine.this) {
                        if(stopped) {
                            break;
                        }
                        try {
                            Engine.this.wait();
                        } catch (InterruptedException e) {
                            if(stopped) {
                                break;
                            }
                            e.printStackTrace();
                        }
                    }
                } else {
                    synchronized (modelLock) {
                        if (modelChanged) {
                            modelChanged = false;
                            lastShownLetterIndex = 0;
                            render.resetArrowFrame();
                        }
                        if (!currentModelWithoutMessage.getState().equals(RenderState.ONLY_BACKGROUND) &&
                                lastShownLetterIndex <= currentMessage.length() - 1) {
                            if(!Character.isSpaceChar(currentMessage.charAt(lastShownLetterIndex))) {
                                soundHandler.playBleep(bleepName);
                            }
                            currentModelWithoutMessage.setText(currentMessage.substring(0,
                                    lastShownLetterIndex + 1));
                            lastShownLetterIndex += 1;
                        }
                        else {
                            if(currentModelWithoutMessage.getState().equals(RenderState.NO_ARROW)) {
                                currentModelWithoutMessage.setState(RenderState.FULL);
                            }
                        }
                        render.draw(canvas, currentModelWithoutMessage);
                    }
                    if(sfxName != null && !sfxPlayed) {
                        soundHandler.playSfx(sfxName);
                        sfxPlayed = true;
                        sfxName = null;
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(TEXT_SPEED);
                    } catch (InterruptedException e) {
                        if(stopped) {
                            break;
                        }
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public Engine(SurfaceView surfaceView, DataDirectory dataDirectory, UIDesign design) {
        this.surfaceView = surfaceView;
        this.surfaceHolder = surfaceView.getHolder();

        this.surfaceCallback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                surfaceHolder = surfaceView.getHolder();
                synchronized (Engine.this) {
                    Engine.this.notifyAll();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width,
                                       int height) {
                surfaceHolder = surfaceView.getHolder();
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                surfaceHolder = surfaceView.getHolder();

            }
        };
        this.surfaceView.getHolder().addCallback(surfaceCallback);

        this.soundHandler = SoundHandler.getInstance();

        this.dataDirectory = dataDirectory;
        this.design = design;
        Bitmap[] arrowFrames = null;
        try {
            arrowFrames = GifUtil.decodeGifIntoBitmapArray(design.getArrowFile());
        } catch (IOException e) {
            log.error("Failed to decode arrow.gif");
        }
        Wini settings = design.getSettings();
        Profile.Section placementSection = settings.get("Placement");
        int boxNameXOffset = Integer.parseInt(placementSection.get("edit_name_leftdiv", "0"));
        int boxNameYOffset = Integer.parseInt(placementSection.get("edit_name_updiv", "0"));
        int textXOffset = Integer.parseInt(placementSection.get("memo_textbox_leftdiv", "0"));
        int textYOffset = Integer.parseInt(placementSection.get("memo_textbox_updiv", "0"));
        int arrowYOffset = Integer.parseInt(placementSection.get("arrow_updiv", "0"));
        int arrowXOffset = Integer.parseInt(placementSection.get("arrow_leftdiv", "0"));
        this.render = Render.builder()
                .view(surfaceView)
                .boxNameXOffset(boxNameXOffset)
                .boxNameYOffset(boxNameYOffset)
                .textXOffset(textXOffset)
                .textYOffset(textYOffset)
                .arrowXOffset(arrowXOffset)
                .arrowYOffset(arrowYOffset)
                .arrowGifSequence(arrowFrames)
                .boxNameFontSize(40)
                .textFontSize(35)
                .build();

        this.runThread = new RunThread();
        this.runThread.start();
    }

    public void handle(MSCommand command) {
        String nameToShow = null;
        String bleepName = null;
        BoxName boxName = BoxName.fromString(command.getBoxName());
        try {
            CharacterData characterData =
                    dataDirectory.getCharacterData(command.getCharacterName());
            switch (boxName) {
                case CHARACTER_NAME:
                    nameToShow = characterData.getShowName();
                    break;
                case MYSTERYNAME:
                    nameToShow = characterData.getMysteryName();
                    break;
                case USERNAME:
                    nameToShow = command.getBoxName();
                    break;
            }
            bleepName = characterData.getBlipsFileName();
        } catch (Exception ex) {
            if (boxName.equals(BoxName.USERNAME)) {
                nameToShow = command.getBoxName();
            } else {
                nameToShow = "???";
            }
        }
        Sprite sprite = dataDirectory.getSprite(command.getCharacterName(),
                command.getSpriteName());

        if (!backgroundToPositionsMap.containsKey(command.getBackgroundImageName())) {
            backgroundToPositionsMap.put(command.getBackgroundImageName(), new Positions());
        }

        Positions positions = backgroundToPositionsMap.get(command.getBackgroundImageName());
        if (command.getPosition().equals(SpritePosition.LEFT)) {
            if(positions.getRightSprite() != null && positions.getRightSprite().getName().equals(sprite.getName())) {
                positions.setRightSprite(null);
            }
            positions.setLeftSprite(sprite);
            positions.setLeftSpriteFlip(command.getFlip());
        } else if (command.getPosition().equals(SpritePosition.RIGHT)) {
            if(positions.getLeftSprite() != null && positions.getLeftSprite().getName().equals(sprite.getName())) {
                positions.setLeftSprite(null);
            }
            positions.setRightSprite(sprite);
            positions.setRightSpriteFlip(command.getFlip());
        }

        List<RenderModel.SpriteDrawInfo> infoList = new LinkedList<>();
        if (command.getPosition().equals(SpritePosition.CENTER)) {

            infoList.add(new RenderModel.SpriteDrawInfo(sprite.getSpriteFile(),
                    SpritePosition.CENTER, command.getFlip()));
        } else {
            Sprite left = positions.getLeftSprite();
            Sprite right = positions.getRightSprite();
            if (left != null) {
                infoList.add(new RenderModel.SpriteDrawInfo(left.getSpriteFile(),
                        SpritePosition.LEFT, positions.getLeftSpriteFlip()));
            }
            if (right != null) {
                infoList.add(new RenderModel.SpriteDrawInfo(right.getSpriteFile(),
                        SpritePosition.RIGHT, positions.getRightSpriteFlip()));
            }
        }


        synchronized (modelLock) {
            this.bleepName = bleepName;
            this.sfxName = command.getSfx().isEmpty() ? null : command.getSfx();
            this.sfxPlayed = false;
            currentMessage = command.getMessage();
            currentModelWithoutMessage = RenderModel.builder()
                    .state(RenderState.NO_ARROW)
                    .boxName(nameToShow)
                    .text(command.getMessage())
                    .textColor(surfaceView.getResources().getColor(UIUtil.getColorId(command.getMessageColor())))
                    .textBoxFile(design.getChatBoxFile())
                    .backgroundFile(dataDirectory.getBackgroundFile(command.getBackgroundImageName()))
                    .spriteDrawInfo(infoList)
                    .build();

            modelChanged = true;
        }

        synchronized (this) {
            notifyAll();
        }
    }

    public void showBackground(String backgroundName) {
        synchronized (modelLock) {
            currentModelWithoutMessage = RenderModel.builder()
                    .state(RenderState.ONLY_BACKGROUND)
                    .backgroundFile(dataDirectory.getBackgroundFile(backgroundName))
                    .build();
            modelChanged = true;
        }

        synchronized (this) {
            notifyAll();
        }
    }

    public void clearPositions() {
        backgroundToPositionsMap.clear();
    }

    public void stop() {
        synchronized (this) {
            stopped = true;
            runThread.interrupt();
            notifyAll();
        }
        try {
            runThread.join();
        } catch (InterruptedException e) {
            log.error("While joining drawer thread: ", e);
        }
    }
}
