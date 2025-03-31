package dev.yatloaf.modkrowd.cubekrowd.common;

import dev.yatloaf.modkrowd.util.Util;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.Map;

public enum CKColor {
    BLACK(TextColor.fromFormatting(Formatting.BLACK)),
    DARK_BLUE(TextColor.fromFormatting(Formatting.DARK_BLUE)),
    DARK_GREEN(TextColor.fromFormatting(Formatting.DARK_GREEN)),
    DARK_AQUA(TextColor.fromFormatting(Formatting.DARK_AQUA)),
    DARK_RED(TextColor.fromFormatting(Formatting.DARK_RED)),
    DARK_PURPLE(TextColor.fromFormatting(Formatting.DARK_PURPLE)),
    GOLD(TextColor.fromFormatting(Formatting.GOLD)),
    GRAY(TextColor.fromFormatting(Formatting.GRAY)),
    DARK_GRAY(TextColor.fromFormatting(Formatting.DARK_GRAY)),
    BLUE(TextColor.fromFormatting(Formatting.BLUE)),
    GREEN(TextColor.fromFormatting(Formatting.GREEN)),
    AQUA(TextColor.fromFormatting(Formatting.AQUA)),
    RED(TextColor.fromFormatting(Formatting.RED)),
    LIGHT_PURPLE(TextColor.fromFormatting(Formatting.LIGHT_PURPLE)),
    YELLOW(TextColor.fromFormatting(Formatting.YELLOW)),
    WHITE(TextColor.fromFormatting(Formatting.WHITE)),
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
