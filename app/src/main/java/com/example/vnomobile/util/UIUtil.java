package com.example.vnomobile.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.content.res.AppCompatResources;

import com.example.vnolib.command.servercommands.enums.MessageColor;
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
        return AppCompatResources.getDrawable(context, colorEnumToResIdMap.get(color));
    }
}
