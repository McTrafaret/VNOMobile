package xyz.udalny.vnomobile.resource.design;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.vnomobile.R;

import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.concurrent.ExecutionException;

import lombok.Getter;
import xyz.udalny.vnolib.command.servercommands.enums.SpriteFlip;
import xyz.udalny.vnolib.command.servercommands.enums.SpritePosition;
import xyz.udalny.vnomobile.util.FileUtil;

@Getter
public class UIDesign {

    protected File designDirectory;
    protected EnumMap<SpritePosition, File> positionToFileMap;
    protected EnumMap<SpriteFlip, File> flipToFileMap;
    protected File[] sfxButtonsFiles;
    protected File[] emoteSelectFiles;
    protected File arrowFile;
    protected File backdropFile;
    protected File chatBoxFile;
    protected Wini settings;

    public void loadPositionButtonIntoView(SpritePosition position, Context context, ImageView view) {
        Glide.with(context)
                .load(positionToFileMap.get(position))
                .into(view);
    }

    public void loadFlipButtonIntoView(SpriteFlip flip, Context context, ImageView view) {
        Glide.with(context)
                .load(flipToFileMap.get(flip))
                .into(view);
    }

    public void loadSfxButtonIntoView(Boolean pressed, Context context, ImageView view) {
        File file = sfxButtonsFiles[pressed ? 1 : 0];
        Glide.with(context)
                .load(file)
                .into(view);
    }

    public Bitmap getBackdropBitmap(View view) throws ExecutionException, InterruptedException {
        return Glide.with(view)
                .asBitmap()
                .load(backdropFile)
                .error(R.drawable.saul_icon)
                .submit()
                .get();
    }

    public Bitmap getChatBoxBitmap(View view) throws ExecutionException, InterruptedException {
        return Glide.with(view)
                .asBitmap()
                .load(chatBoxFile)
                .error(R.drawable.saul_icon)
                .submit()
                .get();
    }

    public UIDesign(File designDirectory) {
        Wini settings1;
        this.designDirectory = designDirectory;
        this.positionToFileMap = createPositionToButtonFileMap(designDirectory);
        this.flipToFileMap = createFlipToButtonFileMap(designDirectory);
        this.sfxButtonsFiles = retrieveSfxButtonsFiles(designDirectory);
        this.emoteSelectFiles = retrieveEmoteSelectFiles(designDirectory);
        this.arrowFile = retrieveArrowFile(designDirectory);
        this.backdropFile = retrieveBackdropFile(designDirectory);
        this.chatBoxFile = retrieveChatBoxFile(designDirectory);
        try {
            settings1 = parseSettings(designDirectory);
        } catch (IOException e) {
            settings1 = null;
            e.printStackTrace();
        }
        this.settings = settings1;
    }

    private static EnumMap<SpritePosition, File> createPositionToButtonFileMap(File designDirectory) {
        File UIButtonsDirectory = FileUtil.getCaseInsensitiveSubFile(designDirectory, "buttons");
        EnumMap<SpritePosition, File> map = new EnumMap<>(SpritePosition.class);
        File leftFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "side");
        File centerFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "side_mid");
        File rightFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "side_on");

        // TODO : add checks if any of this files exist and replace with some default bitmaps maybe
        map.put(SpritePosition.LEFT, leftFile);
        map.put(SpritePosition.CENTER, centerFile);
        map.put(SpritePosition.RIGHT, rightFile);
        return map;
    }

    private static EnumMap<SpriteFlip, File> createFlipToButtonFileMap(File designDirectory) {
        EnumMap<SpriteFlip, File> map = new EnumMap<>(SpriteFlip.class);
        File UIButtonsDirectory = FileUtil.getCaseInsensitiveSubFile(designDirectory, "buttons");
        File noFlipFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "mirror");
        File flipFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "mirror_on");

        map.put(SpriteFlip.NOFLIP, noFlipFile);
        map.put(SpriteFlip.FLIP, flipFile);
        return map;
    }

    private static File[] retrieveSfxButtonsFiles(File designDirectory) {
        File[] files = new File[2];
        File UIButtonsDirectory = FileUtil.getCaseInsensitiveSubFile(designDirectory, "buttons");
        File sfxOnFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "sfx_on");
        File sfxOffFile = FileUtil.getCaseInsensitiveSubFileDropExtension(UIButtonsDirectory, "sfx_off");

        files[0] = sfxOffFile;
        files[1] = sfxOnFile;

        return files;
    }

    private static File[] retrieveEmoteSelectFiles(File designDirectory) {
        File[] files = new File[2];
        File emoteSelectedFile = FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "emoteselected");
        File emoteUnselectedFile = FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "emoteunselected");

        files[0] = emoteUnselectedFile;
        files[1] = emoteSelectedFile;

        return files;
    }

    // TODO: arrow is a gif, so we need to think this
    private static File retrieveArrowFile(File designDirectory) {
        return FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "arrow");
    }

    private static File retrieveBackdropFile(File designDirectory) {
        return FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "backdrop");
    }

    private static File retrieveChatBoxFile(File designDirectory) {
        return FileUtil.getCaseInsensitiveSubFileDropExtension(designDirectory, "chat");
    }

    private static Wini parseSettings(File designDirectory) throws IOException {
        File settingsFile = FileUtil.getCaseInsensitiveSubFile(designDirectory, "design.ini");
        return new Wini(settingsFile);
    }
}
