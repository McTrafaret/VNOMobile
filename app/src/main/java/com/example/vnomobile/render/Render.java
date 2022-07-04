package com.example.vnomobile.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import lombok.Builder;

@Builder
public class Render {

    private static final float SPACING_MULTIPLIER = 1;
    private static final float SPACING_ADDITION = 0;
    private static final boolean INCLUDE_SPACING = false;


    private final int boxNameXOffset;
    private final int boxNameYOffset;
    private final int boxNameFontSize;

    private final int textXOffset;
    private final int textYOffset;
    private final int textFontSize;


    public void draw(Canvas canvas, RenderModel model) {
        canvas.drawBitmap(model.getBackground(), 0, 0, null);
        for(RenderModel.SpriteDrawInfo spriteInfo : model.getSpriteDrawInfo()) {
            int offset = spriteInfo.getSpriteBitmap().getWidth() / 4;
            switch (spriteInfo.getPosition()) {
                case LEFT:
                    offset = -offset;
                case RIGHT:
                    break;
                case CENTER:
                    offset = 0;
            }
            canvas.drawBitmap(spriteInfo.getSpriteBitmap(), offset, 0, null);
        }
        canvas.drawBitmap(model.getTextBox(), 0, canvas.getHeight() - model.getTextBox().getHeight(), null);

        Paint textBoxPaint = new Paint();
        textBoxPaint.setColor(Color.WHITE);
        textBoxPaint.setTextSize(boxNameFontSize);
        canvas.drawText(model.getBoxName(), boxNameXOffset, boxNameYOffset, textBoxPaint);

        TextPaint textPaint = new TextPaint();
        textPaint.setColor(model.getTextColor());
        textPaint.setTextSize(textFontSize);
        canvas.drawText(model.getBoxName(), textXOffset, textYOffset, textPaint);

        StaticLayout staticLayout;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            staticLayout = StaticLayout.Builder.obtain(model.getText(), 0, model.getText().length() - 1, textPaint, canvas.getWidth() - textXOffset)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(SPACING_ADDITION, SPACING_MULTIPLIER)
                    .setIncludePad(INCLUDE_SPACING)
                    .setMaxLines(5)
                    .build();
        }
        else {
            staticLayout = new StaticLayout(model.getText(),
                    textPaint,
                    canvas.getWidth() - textXOffset,
                    Layout.Alignment.ALIGN_NORMAL,
                    SPACING_MULTIPLIER,
                    SPACING_ADDITION,
                    INCLUDE_SPACING);
        }
        canvas.save();
        canvas.translate(-textXOffset, -textYOffset);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
