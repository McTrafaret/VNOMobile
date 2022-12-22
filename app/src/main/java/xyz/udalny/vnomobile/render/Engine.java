package xyz.udalny.vnomobile.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import org.ini4j.Profile;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import xyz.udalny.vnolib.client.model.BoxName;
import xyz.udalny.vnolib.command.servercommands.MSCommand;
import xyz.udalny.vnolib.command.servercommands.enums.SpriteFlip;
import xyz.udalny.vnolib.command.servercommands.enums.SpritePosition;
import xyz.udalny.vnomobile.resource.CharacterData;
import xyz.udalny.vnomobile.resource.DataDirectory;
import xyz.udalny.vnomobile.resource.SoundHandler;
import xyz.udalny.vnomobile.resource.design.UIDesign;
import xyz.udalny.vnomobile.resource.sprite.Sprite;
import xyz.udalny.vnomobile.util.GifUtil;
import xyz.udalny.vnomobile.util.UIUtil;

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
                if (surfaceHolder == null || currentModelWithoutMessage == null || (canvas = surfaceHolder.lockCanvas()) == null) {
                    synchronized (Engine.this) {
                        if (stopped) {
                            break;
                        }
                        try {
                            Engine.this.wait();
                        } catch (InterruptedException e) {
                            if (stopped) {
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
                            if (!Character.isSpaceChar(currentMessage.charAt(lastShownLetterIndex))) {
                                soundHandler.playBleep(bleepName);
                            }
                            currentModelWithoutMessage.setText(currentMessage.substring(0,
                                    lastShownLetterIndex + 1));
                            lastShownLetterIndex += 1;
                        } else {
                            if (currentModelWithoutMessage.getState().equals(RenderState.NO_ARROW)) {
                                currentModelWithoutMessage.setState(RenderState.FULL);
                            }
                        }
                        render.draw(canvas, currentModelWithoutMessage);
                    }
                    if (sfxName != null && !sfxPlayed) {
                        soundHandler.playSfx(sfxName);
                        sfxPlayed = true;
                        sfxName = null;
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(TEXT_SPEED);
                    } catch (InterruptedException e) {
                        if (stopped) {
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

    private String getNameToShow(CharacterData characterData, String boxNameString) {
        String nameToShow = null;
        BoxName boxName = BoxName.fromString(boxNameString);
        try {
            switch (boxName) {
                case CHARACTER_NAME:
                    nameToShow = characterData.getShowName();
                    break;
                case MYSTERYNAME:
                    nameToShow = characterData.getMysteryName();
                    break;
                case USERNAME:
                    nameToShow = boxNameString;
                    break;
            }
        } catch (Exception ex) {
            nameToShow = "???";
        }
        return nameToShow;
    }

    private String getBleepName(CharacterData characterData) {
        return characterData.getBlipsFileName();
    }

    private Positions handlePositions(Sprite newSprite,
                                      SpritePosition spritePosition,
                                      SpriteFlip spriteFlip,
                                      String backgroundImageName) {
        if (!backgroundToPositionsMap.containsKey(backgroundImageName)) {
            backgroundToPositionsMap.put(backgroundImageName, new Positions());
        }

        Positions positions = backgroundToPositionsMap.get(backgroundImageName);
        if (spritePosition.equals(SpritePosition.LEFT)) {
            if (positions.getRightSprite() != null && positions.getRightSprite().getName().equals(newSprite.getName())) {
                positions.setRightSprite(null);
            }
            positions.setLeftSprite(newSprite);
            positions.setLeftSpriteFlip(spriteFlip);
        } else if (spritePosition.equals(SpritePosition.RIGHT)) {
            if (positions.getLeftSprite() != null && positions.getLeftSprite().getName().equals(newSprite.getName())) {
                positions.setLeftSprite(null);
            }
            positions.setRightSprite(newSprite);
            positions.setRightSpriteFlip(spriteFlip);
        }

        return positions;
    }

    public List<RenderModel.SpriteDrawInfo> createListOfThingsToRender(Sprite newSprite,
                                                                       SpritePosition newSpritePosition,
                                                                       SpriteFlip newSpriteFlip,
                                                                       Positions otherSpritePositions) {
        List<RenderModel.SpriteDrawInfo> infoList = new LinkedList<>();
        if (newSpritePosition.equals(SpritePosition.CENTER)) {

            infoList.add(new RenderModel.SpriteDrawInfo(newSprite,
                    SpritePosition.CENTER, newSpriteFlip));
        } else {
            Sprite left = otherSpritePositions.getLeftSprite();
            Sprite right = otherSpritePositions.getRightSprite();
            if (left != null) {
                infoList.add(new RenderModel.SpriteDrawInfo(left,
                        SpritePosition.LEFT, otherSpritePositions.getLeftSpriteFlip()));
            }
            if (right != null) {
                infoList.add(new RenderModel.SpriteDrawInfo(right,
                        SpritePosition.RIGHT, otherSpritePositions.getRightSpriteFlip()));
            }
        }

        return infoList;
    }

    public void handle(MSCommand command) {
        String nameToShow = null;
        String bleepName = null;
        try {
            CharacterData characterData =
                    dataDirectory.getCharacterData(command.getCharacterName());
            nameToShow = getNameToShow(characterData, command.getBoxName());
            bleepName = getBleepName(characterData);
        } catch (Exception ex) {
            nameToShow = "???";
        }
        Sprite sprite = dataDirectory.getSprite(command.getCharacterName(),
                command.getSpriteName());

        Positions positions = handlePositions(
                sprite,
                command.getPosition(),
                command.getFlip(),
                command.getBackgroundImageName());

        List<RenderModel.SpriteDrawInfo> spritesToDraw = createListOfThingsToRender(
                sprite,
                command.getPosition(),
                command.getFlip(),
                positions
        );


        File background = dataDirectory.getBackgroundFile(command.getBackgroundImageName());
        if (background == null) {
            background = design.getBackdropFile();
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
                    .backgroundFile(background)
                    .spriteDrawInfo(spritesToDraw)
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

    public void showUrl(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap;
                try {
                    bitmap = Glide.with(surfaceView)
                            .asBitmap()
                            .load(url)
                            .submit()
                            .get();
                } catch (Exception ex) {
                    log.error("Failed to load an image from {}: ", url, ex);
                    return;
                }

                Canvas canvas = surfaceHolder.lockCanvas();
                Paint antiAliasPaint = new Paint();
                antiAliasPaint.setAntiAlias(true);
                antiAliasPaint.setFilterBitmap(true);
                Rect canvasRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.drawBitmap(bitmap, null, canvasRect, antiAliasPaint);
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }).start();
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
