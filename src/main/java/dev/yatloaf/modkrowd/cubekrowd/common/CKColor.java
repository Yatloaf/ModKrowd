package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import java.util.Map;

public enum CKColor {
    BLACK(TextColor.BLACK),
    DARK_BLUE(TextColor.DARK_BLUE),
    DARK_GREEN(TextColor.DARK_GREEN),
    DARK_AQUA(TextColor.DARK_AQUA),
    DARK_RED(TextColor.DARK_RED),
    DARK_PURPLE(TextColor.DARK_PURPLE),
    GOLD(TextColor.GOLD),
    GRAY(TextColor.GRAY),
    DARK_GRAY(TextColor.DARK_GRAY),
    BLUE(TextColor.BLUE),
    GREEN(TextColor.GREEN),
    AQUA(TextColor.AQUA),
    RED(TextColor.RED),
    LIGHT_PURPLE(TextColor.LIGHT_PURPLE),
    YELLOW(TextColor.YELLOW),
    WHITE(TextColor.WHITE),
    INDIGO(TextColor.fromRgb(0x864DEB)),
    AZURE(TextColor.fromRgb(0x03A9F4)),
    DINOCOIN(TextColor.fromRgb(0xFFC414)),
    DIRT(TextColor.fromRgb(0xBF8240)),
    SKY(TextColor.fromRgb(0x60C6FF)),
    CRIMSON(TextColor.fromRgb(0xFF5F79)),
    SILVER(TextColor.fromRgb(0xD5D5D5)),
    SLEET(TextColor.fromRgb(0x959595)),
    LAVENDER(TextColor.fromRgb(0xC26EFF)),
    DEPRESSED_DARK_AQUA(TextColor.fromRgb(0x069696)),
    DEPRESSED_GOLD(TextColor.fromRgb(0xD48F04)),
    DEPRESSED_LAVENDER(TextColor.fromRgb(0x9F63C9)),
    ;

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
