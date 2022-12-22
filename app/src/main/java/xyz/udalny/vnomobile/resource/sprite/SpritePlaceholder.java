package xyz.udalny.vnomobile.resource.sprite;

import android.graphics.Bitmap;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.vnomobile.R;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpritePlaceholder extends Sprite {

    public SpritePlaceholder() {
        this.name = "Saul";
        this.spriteFile = null;
    }

    @Override
    public Bitmap getBitmap(View view) {
        try {
            return Glide.with(view)
                    .asBitmap()
                    .load(R.drawable.saul_sprite)
                    .submit()
                    .get();
        }
        catch (Exception ex) {
            log.error("Failed to get placeholder: ", ex);
        }
        return null;
    }
}
