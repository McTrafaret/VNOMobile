package xyz.udalny.vnomobile.util;

import android.graphics.Bitmap;

import com.waynejo.androidndkgif.GifDecoder;

import java.io.File;
import java.io.IOException;

public class GifUtil {

    public static Bitmap[] decodeGifIntoBitmapArray(File gifFile) throws IOException {
        GifDecoder decoder = new GifDecoder();
        boolean isSucceeded = decoder.load(gifFile.getPath());
        if (!isSucceeded) {
            return null;
        }
        int numberOfFrames = decoder.frameNum();
        Bitmap[] bitmapArray = new Bitmap[numberOfFrames];
        for (int i = 0; i < numberOfFrames; i++) {
            Bitmap frameBitmap = decoder.frame(i);
            bitmapArray[i] = frameBitmap;
        }
        return bitmapArray;
    }
}