package xyz.udalny.vnomobile.render;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.view.View;

import xyz.udalny.vnolib.command.servercommands.enums.SpriteFlip;
import xyz.udalny.vnomobile.util.FileUtil;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class Render {

    private static final float SPACING_MULTIPLIER = 1;
    private static final float SPACING_ADDITION = 0;
    private static final boolean INCLUDE_SPACING = false;

    private static final int DEFAULT_BOX_NAME_X_OFFSET = 20;
    private static final int DEFAULT_BOX_NAME_Y_OFFSET = 195;
    private static final int DEFAULT_TEXT_X_OFFSET = 20;
    private static final int DEFAULT_TEXT_Y_OFFSET = 210;
    private static final int DEFAULT_ARROW_X_OFFSET = 430;
    private static final int DEFAULT_ARROW_Y_OFFSET = 250;


    private final View view;

    private final Bitmap[] arrowGifSequence;

    private final int boxNameXOffset;
    private final int boxNameYOffset;
    private final int boxNameFontSize;

    private final int textXOffset;
    private final int textYOffset;
    private final int textFontSize;

    private final int arrowXOffset;
    private final int arrowYOffset;

    private int arrowFrame = 0;


    public void resetArrowFrame() {
        arrowFrame = 0;
    }

    private void nextArrowFrame() {
        arrowFrame = (arrowFrame + 1) % arrowGifSequence.length;
    }


    public void draw(Canvas canvas, RenderModel model) {
        Paint antiAliasPaint = new Paint();
        antiAliasPaint.setAntiAlias(true);
        antiAliasPaint.setFilterBitmap(true);
        Rect canvasRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
        Bitmap backgroundBitmap = FileUtil.loadBitmapFromFile(view, model.getBackgroundFile());
        canvas.drawBitmap(backgroundBitmap, null, canvasRect, antiAliasPaint);
        if(model.getState().equals(RenderState.ONLY_BACKGROUND)) {
            return;
        }
        for(RenderModel.SpriteDrawInfo spriteInfo : model.getSpriteDrawInfo()) {
            int offset = canvas.getWidth() / 4;
            switch (spriteInfo.getPosition()) {
                case LEFT:
                    offset = -offset;
                case RIGHT:
                    break;
                case CENTER:
                    offset = 0;
            }
            Rect spriteRect = new Rect(offset, 0, canvas.getWidth() + offset, canvas.getHeight());
            Bitmap spriteBitmap = FileUtil.loadBitmapFromFile(view, spriteInfo.getSpriteFile());
            if(spriteInfo.getFlip().equals(SpriteFlip.FLIP)) {
                Matrix matrix = new Matrix();
                matrix.postScale(-1, 1, spriteBitmap.getWidth() / 2f, spriteBitmap.getHeight() / 2f);
                spriteBitmap = Bitmap.createBitmap(spriteBitmap, 0, 0, spriteBitmap.getWidth(), spriteBitmap.getHeight(), matrix, true);
            }
            canvas.drawBitmap(spriteBitmap, null, spriteRect, antiAliasPaint);
        }
        if(model.getText().trim().isEmpty()) {
            return;
        }
        Bitmap textBoxBitmap = FileUtil.loadBitmapFromFile(view, model.getTextBoxFile());
        int boxHeight = canvas.getHeight() * textBoxBitmap.getHeight() / backgroundBitmap.getHeight();
        Rect boxRect = new Rect(0, canvas.getHeight() - boxHeight, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(textBoxBitmap, null, boxRect, antiAliasPaint);

        DisplayMetrics metrics = view.getResources().getDisplayMetrics();

        float yScaleRatio = (float) canvas.getHeight() / backgroundBitmap.getHeight();
        float xScaleRatio = (float) canvas.getWidth() / backgroundBitmap.getWidth();
        float xOffset, yOffset;

        TextPaint textBoxPaint = new TextPaint();
        textBoxPaint.setAntiAlias(true);
        textBoxPaint.setColor(Color.WHITE);
        textBoxPaint.setTextSize(boxNameFontSize);
        xOffset = (boxNameXOffset + DEFAULT_BOX_NAME_X_OFFSET) * xScaleRatio;
        yOffset = (boxNameYOffset + DEFAULT_BOX_NAME_Y_OFFSET) * yScaleRatio;
        canvas.drawText(model.getBoxName(), xOffset, yOffset, textBoxPaint);

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(model.getTextColor());
        textPaint.setTextSize(textFontSize);
        textPaint.setAntiAlias(true);

        StaticLayout staticLayout;
        xOffset = (textXOffset + DEFAULT_TEXT_X_OFFSET) * xScaleRatio;
        yOffset = (textYOffset + DEFAULT_TEXT_Y_OFFSET) * yScaleRatio;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            staticLayout = StaticLayout.Builder.obtain(model.getText(), 0, model.getText().length(), textPaint, canvas.getWidth() - (int)xOffset)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(SPACING_ADDITION, SPACING_MULTIPLIER)
                    .setIncludePad(INCLUDE_SPACING)
                    .setMaxLines(5)
                    .build();
        }
        else {
            staticLayout = new StaticLayout(model.getText(),
                    textPaint,
                    canvas.getWidth() - (int)xOffset,
                    Layout.Alignment.ALIGN_NORMAL,
                    SPACING_MULTIPLIER,
                    SPACING_ADDITION,
                    INCLUDE_SPACING);
        }
        canvas.save();
        canvas.translate(xOffset, yOffset);
        staticLayout.draw(canvas);
        canvas.restore();
        if(model.getState().equals(RenderState.NO_ARROW) || arrowGifSequence == null) {
            return;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(metrics.scaledDensity, metrics.scaledDensity);
        xOffset = (arrowXOffset + DEFAULT_ARROW_X_OFFSET) * xScaleRatio;
        yOffset = (arrowYOffset + DEFAULT_ARROW_Y_OFFSET) * yScaleRatio;
        matrix.postTranslate(xOffset, yOffset);

        canvas.drawBitmap(arrowGifSequence[arrowFrame], matrix, antiAliasPaint);
        nextArrowFrame();
    }
}
