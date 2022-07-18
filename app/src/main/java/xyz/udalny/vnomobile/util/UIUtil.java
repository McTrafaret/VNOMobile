package xyz.udalny.vnomobile.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import xyz.udalny.vnolib.command.servercommands.enums.MessageColor;
import com.example.vnomobile.R;

import java.util.EnumMap;

public class UIUtil {

    public static final EnumMap<MessageColor, Integer> colorEnumToResIdMap = new EnumMap<MessageColor, Integer>(MessageColor.class);
    static {
        colorEnumToResIdMap.put(MessageColor.WHITE, R.color.messageWhite);
        colorEnumToResIdMap.put(MessageColor.BLUE, R.color.messageBlue);
        colorEnumToResIdMap.put(MessageColor.PINK, R.color.messagePink);
        colorEnumToResIdMap.put(MessageColor.YELLOW, R.color.messageYellow);
        colorEnumToResIdMap.put(MessageColor.GREEN, R.color.messageGreen);
        colorEnumToResIdMap.put(MessageColor.ORANGE, R.color.messageOrange);
        colorEnumToResIdMap.put(MessageColor.RED, R.color.messageRed);
    }

    public static Drawable getColor(Context context, MessageColor color) {
        Integer colorId = colorEnumToResIdMap.get(color);
        if(colorId == null) {
            return null;
        }
        return AppCompatResources.getDrawable(context, colorId);
    }

    public static int getColorId(MessageColor color) {
        return colorEnumToResIdMap.get(color);
    }
}
