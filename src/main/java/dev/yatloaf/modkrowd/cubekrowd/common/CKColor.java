package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Map;

public enum CKColor {
    BLACK(TextColor.fromLegacyFormat(ChatFormatting.BLACK)),
    DARK_BLUE(TextColor.fromLegacyFormat(ChatFormatting.DARK_BLUE)),
    DARK_GREEN(TextColor.fromLegacyFormat(ChatFormatting.DARK_GREEN)),
    DARK_AQUA(TextColor.fromLegacyFormat(ChatFormatting.DARK_AQUA)),
    DARK_RED(TextColor.fromLegacyFormat(ChatFormatting.DARK_RED)),
    DARK_PURPLE(TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE)),
    GOLD(TextColor.fromLegacyFormat(ChatFormatting.GOLD)),
    GRAY(TextColor.fromLegacyFormat(ChatFormatting.GRAY)),
    DARK_GRAY(TextColor.fromLegacyFormat(ChatFormatting.DARK_GRAY)),
    BLUE(TextColor.fromLegacyFormat(ChatFormatting.BLUE)),
    GREEN(TextColor.fromLegacyFormat(ChatFormatting.GREEN)),
    AQUA(TextColor.fromLegacyFormat(ChatFormatting.AQUA)),
    RED(TextColor.fromLegacyFormat(ChatFormatting.RED)),
    LIGHT_PURPLE(TextColor.fromLegacyFormat(ChatFormatting.LIGHT_PURPLE)),
    YELLOW(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)),
    WHITE(TextColor.fromLegacyFormat(ChatFormatting.WHITE)),
    INDIGO(TextColor.fromRgb(0x864DEB)),
    AZURE(TextColor.fromRgb(0x03A9F4)),
    SKY(TextColor.fromRgb(0x60C6FF)),
    CRIMSON(TextColor.fromRgb(0xFF5F79)),
    FESTIVE_GREEN(TextColor.fromRgb(0x43C800)),
    FESTIVE_RED(TextColor.fromRgb(0xFF4E50));

    public final TextColor textColor;
    public final Style style;

    private static final Map<TextColor, CKColor> FROM_TEXT_COLOR = Util.arrayToMap(values(), key -> key.textColor, value -> value);

    CKColor(TextColor textColor) {
        this.textColor = textColor;
        this.style = Style.EMPTY.withColor(textColor);
    }

    public static CKColor fromTextColor(TextColor textColor) {
        return FROM_TEXT_COLOR.get(textColor);
    }

    public static CKColor fromStyle(Style style) {
        return fromTextColor(style.getColor());
    }
}
